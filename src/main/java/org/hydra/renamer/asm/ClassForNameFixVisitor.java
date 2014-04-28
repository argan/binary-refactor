package org.hydra.renamer.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ClassForNameFixVisitor extends ClassAdapter {
    private Remapper remapper;

    public ClassForNameFixVisitor(ClassVisitor cv, Remapper mapper) {
        super(cv);
        this.remapper = mapper;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions) {
        return new ClassForNameFixMethodVistor(super.visitMethod(access, name, desc, signature,
            exceptions), remapper);
    }

}
