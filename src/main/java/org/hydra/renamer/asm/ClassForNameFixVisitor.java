package org.hydra.renamer.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

public class ClassForNameFixVisitor extends ClassAdapter {
	private Remapper remapper;

	public ClassForNameFixVisitor(ClassVisitor cv, Remapper mapper) {
		super(cv);
		this.remapper = mapper;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		// 直接使用匿名类减少类的数量
		return new MethodAdapter(super.visitMethod(access, name, desc, signature, exceptions)) {
			@Override
			public void visitLdcInsn(Object cst) {
				if (cst instanceof String && cst != null) {
					// 如果是装载一个常量的字符串，检查在不在我们的类名列表里，如果是，就修改掉
					String oldName = ((String) cst).replace('.', '/');
					// Log.debug("visitLdcInsn %s", oldName);
					String newName = remapper.map(oldName);
					if (newName != null && !oldName.equals(newName)) {
						newName = newName.replace('/', '.');
						// Log.debug("change constant loading from %s to %s", cst,
						// newName);
						super.visitLdcInsn(newName);
						return;
					}
				}
				super.visitLdcInsn(cst);
			}
		};
	}

}
