package org.hydra.renamer.asm;

import java.util.HashMap;
import java.util.Map;

import org.hydra.renamer.item.ClassInfo;
import org.hydra.renamer.item.MethodInfo;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;

public class ScanLibVisitor extends EmptyVisitor {
	private static void link(ClassInfo parent, ClassInfo child) {
		if (parent.name == null)
			return;
		parent.children.add(child);
		child.parent.add(parent);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		clazz = map.get(name);
		ClassInfo s = map.get(superName);
		if (s != null)
			link(s, clazz);

		if (interfaces != null) {
			for (String i : interfaces) {
				s = map.get(i);
				if (s != null)
					link(s, clazz);
			}
		}
	}

	ClassInfo clazz;

	@Override
	public void visitEnd() {

	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		return null;
	}

	@SuppressWarnings("serial")
    Map<String, ClassInfo> map = new HashMap<String, ClassInfo>() {

		@Override
		public ClassInfo get(Object key) {
			ClassInfo info = super.get(key);
			if (info == null) {
				info = new ClassInfo();
				info.name = (String) key;
				info.newName = info.name;
				info.renamable = false;
				this.put((String) key, info);
			}
			return info;
		}

	};

	public Map<String, ClassInfo> getClassMap() {
		return new HashMap<String, ClassInfo>(this.map);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if ((access & Opcodes.ACC_PRIVATE) != 0 || (access & Opcodes.ACC_FINAL) != 0 || (access & Opcodes.ACC_STATIC) != 0)
			return null;
		if ("<init>".equals(name) || "<cinit>".equals(name))
			return null;
		MethodInfo method = new MethodInfo();
		clazz.methods.put("method:" + name + desc, method);
		method.name = name;
		method.newName=name;
		method.desc = desc;
		method.access = access;
		method.renamable = false;
		// method.owner = clazz;
		// method.ret = map.get(getTypeName(Type.getReturnType(desc)));
		// Type[] argTypes = Type.getArgumentTypes(desc);
		// if (argTypes != null) {
		// for (Type type : argTypes) {
		// method.args.add(map.get(getTypeName(type)));
		// }
		// }
		return null;

	}

}
