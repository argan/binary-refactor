package org.hydra.renamer.asm;

import org.hydra.renamer.MethodInfo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

public class CollectDepsMethodVisitor extends EmptyVisitor {

    private MethodInfo methodInfo;

    public CollectDepsMethodVisitor(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        // TODO to implement
        return null;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        // TODO to implement
        return null;
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.methodInfo.addDependency(type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.methodInfo.addDependency(owner);
        this.methodInfo.addDependency(getClassName(Type.getType(desc)));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        if (owner.equals(this.methodInfo.getEnclosedClass().getClassName()) == false) {
            this.methodInfo.addDependency(owner);
            this.methodInfo.addDependency(getClassName(Type.getReturnType(desc)));
            Type[] types = Type.getArgumentTypes(desc);
            if (types != null) {
                for (Type t : types) {
                    this.methodInfo.addDependency(getClassName(t));
                }
            }
        }
    }

    private String getClassName(Type type) {
        if (type.getSort() == Type.OBJECT) {
            return type.getInternalName();
        }
        if (type.getSort() == Type.ARRAY) {
            Type elementType = type.getElementType();
            if (elementType.getSort() == Type.OBJECT) {
                return elementType.getInternalName();
            } else {
                return elementType.getClassName();
            }

        }
        return type.getClassName();
    }

    @Override
    public void visitLdcInsn(Object cst) {
        // TODO to detecet name for 'Class.forName' ?

    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        this.methodInfo.addDependency(Type.getType(desc).getInternalName());
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.methodInfo.addDependency(type);
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        this.methodInfo.addDependency(Type.getType(desc).getInternalName());
    }

}
