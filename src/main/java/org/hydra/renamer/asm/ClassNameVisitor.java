package org.hydra.renamer.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;

public class ClassNameVisitor extends ClassAdapter {

	String name;

	public ClassNameVisitor(ClassVisitor cv) {
		super(cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.name = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}

	public String getName() {
		return name;
	}
}
