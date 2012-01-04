package org.hydra.renamer.asm;

import org.hydra.renamer.ClassInfo;
import org.hydra.renamer.ClassMap;
import org.hydra.util.Lists.Pair;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

public class CollectClassInfoVisitor extends EmptyVisitor {
    private ClassMap map;
    private String className;

    public CollectClassInfoVisitor(ClassMap map) {
        this.map = map;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        boolean isInterface = (access & 0x0200) == 1;
        ClassInfo info = new ClassInfo(name, isInterface);
        if (interfaces != null) {
            for (String a : interfaces) {
                info.getInterfaces().add(new ClassInfo(a, true));
            }
        }
        if (!"java/lang/Object".equals(superName)) {
            info.setSuperClass(new ClassInfo(superName, isInterface));
        }
        this.className = name;
        this.map.addClassInfo(info);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (!"serialVersionUID".equals(name)) {
            this.map.getClassInfo(this.className).getFields().add(new Pair<String, String>(name, desc));
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (!name.startsWith("<")) {
            this.map.getClassInfo(this.className).getMethods().add(new Pair<String, String>(name, desc));
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

}
