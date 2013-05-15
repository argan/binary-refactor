package org.hydra.renamer.asm;

import org.hydra.renamer.ClassInfo;
import org.hydra.renamer.ClassMap;
import org.hydra.renamer.FieldInfo;
import org.hydra.renamer.MethodInfo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

public class CollectClassInfoVisitor extends EmptyVisitor {
    private ClassMap map;
    private String className;
    private ClassInfo info;

    public CollectClassInfoVisitor(ClassMap map) {
        this.map = map;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        info = new ClassInfo(this.map, name, access);
        if (interfaces != null) {
            for (String a : interfaces) {
                info.addInterface(a);
            }
        }
        if (!"java/lang/Object".equals(superName)) {
            info.setSuperClass(superName);
        }
        this.className = name;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (!"serialVersionUID".equals(name)) {
            this.map.getClassInfo(this.className).addField(new FieldInfo(name, desc, access,value));
        }
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodInfo methodInfo = new MethodInfo(name, desc, access, exceptions);
        methodInfo.setExceptions(exceptions);
        this.map.getClassInfo(this.className).addMethod(methodInfo);
        return new CollectDepsMethodVisitor(methodInfo);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        this.info.setOuterClass(name);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        // TODO to implement annotation visitor
        return null;
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.info.addInnerClass(name);
    }

}
