package org.hydra.renamer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.hydra.renamer.RenameConfig.ClassRenameInfo;
import org.hydra.renamer.asm.CollectClassInfoVisitor;
import org.hydra.util.Log;
import org.hydra.util.Utils;
import org.objectweb.asm.ClassReader;

public class ClassMap {
    private Map<String, ClassInfo> map = new HashMap<String, ClassInfo>();

    public ClassInfo getClassInfo(String name) {
        return this.map.get(name);
    }

    public Map<String, List<ClassInfo>> getTree() {
        Map<String, List<ClassInfo>> result = new TreeMap<String, List<ClassInfo>>();

        for (Map.Entry<String, ClassInfo> entry : map.entrySet()) {
            String pkg = getPkg(entry.getKey());
            List<ClassInfo> list = result.get(pkg);
            if (list == null) {
                list = new ArrayList<ClassInfo>();
                result.put(pkg, list);
            }
            list.add(entry.getValue());
        }
        return result;
    }

    private String getPkg(String key) {
        int index = key.lastIndexOf("/");
        String fullName = key.substring(0, index);
        StringBuilder shortName = new StringBuilder();

        String[] arr = fullName.split("/");
        // 除了最后一层包名外，前面的都只取第一个字符
        for (int i = 0; i < arr.length - 1; i++) {
            shortName.append(arr[i].charAt(0)).append("/");
        }

        shortName.append(arr[arr.length - 1]);
        return shortName.toString();
    }

    public void addClassInfo(ClassInfo info) {
        this.map.put(info.getClassName(), info);
    }

    public void rebuildConfig(RenameConfig config, String addPkgs) {
        // build the hierarchy and rebuild the Config
        for (Map.Entry<String, ClassInfo> entry : map.entrySet()) {
            ClassInfo info = entry.getValue();
            if (info.getSuperClass() != null) {
                info.getSuperClass().getChildren().add(info);
            }
            for (ClassInfo intf : info.getInterfaces()) {
                intf.getChildren().add(info);
            }
        }
        List<String> addPkgsList = Arrays.asList(Utils.tokens(addPkgs));
        if (addPkgsList != null) {
            for (String name : map.keySet()) {
                if (!config.getConfig().containsKey(name)) {
                    config.getConfig().put(name, new ClassRenameInfo());
                }
            }
        }
        // 重建 config ，主要是根据类／接口的层次结构修改
        for (Map.Entry<String, ClassRenameInfo> entry : config.getConfig().entrySet()) {
            String clazz = entry.getKey();
            ClassInfo classInfo = map.get(clazz);
            if (classInfo == null) {
                Log.debug("classInfo not found for %s", clazz);
            } else {
                entry.getValue().setSuperClassName(
                        classInfo.getSuperClass() == null ? null : classInfo.getSuperClass().getClassName());
                if (classInfo.getInterfaces().size() > 0) {
                    for (ClassInfo i : classInfo.getInterfaces()) {
                        entry.getValue().addInterface(i.getClassName());
                    }
                }
            }
        }
    }

    public String toString() {
        return String.format("ClassMap {%s}", map);
    }

    public static ClassMap build(JarFile jar) {
        ClassMap map = new ClassMap();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class")) {
                InputStream inputStream = null;
                try {
                    inputStream = jar.getInputStream(jar.getEntry(name));
                    // .class 文件就检查并改写
                    parseClass(inputStream, map);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(inputStream);
                }
            }
        }
        return map;
    }

    private static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                // null
            }
        }
    }

    private static void parseClass(InputStream inputStream, ClassMap map) {
        CollectClassInfoVisitor cv = new CollectClassInfoVisitor(map);
        ClassReader reader;
        try {
            reader = new ClassReader(inputStream);
            reader.accept(cv, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
