package org.hydra.renamer.asm;

import java.util.HashMap;
import java.util.Map;

import org.hydra.renamer.item.ClassInfo;
import org.hydra.renamer.item.FieldInfo;
import org.hydra.renamer.item.MethodInfo;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * 
 * @author Panxiaobo [pxb1988@126.com]
 * 
 */
@SuppressWarnings("serial")
public class ClassMapClassVisitor extends EmptyVisitor {

	Map<String, ClassInfo> map = new HashMap<String, ClassInfo>() {

		@Override
		public ClassInfo get(Object key) {
			ClassInfo info = super.get(key);
			if (info == null) {
				info = new ClassInfo();
				info.name = (String) key;
				this.put((String) key, info);
			}
			return info;
		}

	};
	ClassInfo clazz;

	private static void link(ClassInfo parent, ClassInfo child) {
		parent.children.add(child);
		child.parent.add(parent);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

		clazz = map.get(name);

		link(map.get(superName), clazz);

		if (interfaces != null) {
			for (String i : interfaces) {
				link(map.get(i), clazz);
			}
		}
	}

	@Override
	public void visitEnd() {
		clazz = null;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldInfo field = new FieldInfo();
		clazz.fields.put("field:" + name + desc, field);
		field.name = name;
		field.desc = desc;
		field.value = value;
		field.access = access;
		// field.owner = clazz;
		// field.type = map.get(getTypeName(Type.getType(desc)));
		return null;
	}

	public Map<String, ClassInfo> getClassMap() {
		return new HashMap<String, ClassInfo>(this.map);
	}

	String getTypeName(Type type) {
		if (Type.VOID_TYPE.equals(type))
			return "java/lang/void";
		return type.getInternalName();
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodInfo method = new MethodInfo();
		clazz.methods.put("method:" + name + desc, method);
		method.name = name;
		method.desc = desc;
		method.access = access;
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
