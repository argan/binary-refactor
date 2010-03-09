package org.hydra.renamer;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.hydra.renamer.asm.ClassForNameFixVisitor;
import org.hydra.renamer.asm.ClassMapClassVisitor;
import org.hydra.renamer.asm.ClassNameVisitor;
import org.hydra.renamer.asm.Remapper;
import org.hydra.renamer.item.ClassInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.RemappingClassAdapter;

public class Renamer {

	/**
	 * 收集信息
	 * 
	 * @param files
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ClassInfo> collect(String... files) throws Exception {
		if (files == null)
			return Collections.EMPTY_MAP;
		ClassMapClassVisitor visitor = new ClassMapClassVisitor();
		for (String file : files) {
			ZipFile zip = new ZipFile(file);

			for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class")) {
					new ClassReader(zip.getInputStream(entry)).accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
				}
			}
		}
		return visitor.getClassMap();
	}

	/**
	 * 转换信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, ClassInfo> transform(Map<String, ClassInfo> map) {
		return map;
	}

	/**
	 * 执行修改
	 * 
	 * @param map
	 * @param files
	 * @throws Exception
	 */
	public void action(Map<String, ClassInfo> map, String... files) throws Exception {

		Remapper remapper = new Remapper(map);

		for (String file : files) {
			String newFile = file + ".br.jar";// .br for binary-refactor
			ZipFile zip = new ZipFile(file);
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(newFile)));

			ClassWriter writer = new ClassWriter(0);
			ClassNameVisitor classNameVisitor = new ClassNameVisitor(writer);
			ClassVisitor visitor = new ClassForNameFixVisitor(new RemappingClassAdapter(classNameVisitor, remapper), remapper);

			for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory()) {
					// 目录就直接写入
					zos.putNextEntry(new ZipEntry(entry.getName()));
					zos.closeEntry();
				} else if (entry.getName().endsWith(".class")) {
					new ClassReader(zip.getInputStream(entry)).accept(visitor, 0);
					String className = classNameVisitor.getName();
					ZipEntry newEntry = new ZipEntry(className + ".class");
					zos.putNextEntry(newEntry);
					zos.write(writer.toByteArray());
					zos.closeEntry();
				} else {
					// 其他资源直接写入
					zos.putNextEntry(new ZipEntry(entry.getName()));
					byte[] buff = new byte[10240];
					int cnt = 0;
					InputStream is = zip.getInputStream(entry);
					while ((cnt = is.read(buff)) > 0) {
						zos.write(buff, 0, cnt);
					}
					zos.closeEntry();
				}
			}

			zos.close();
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String... args) throws Exception {
		Renamer renamer = new Renamer();
		Map<String, ClassInfo> map = renamer.collect(args);
		map = renamer.transform(map);
		renamer.action(map, args);
	}
}
