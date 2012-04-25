package org.hydra.matcher;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassSignatureCollector implements ClassVisitor {
    private Map<String, ClassSignature> info = new HashMap<String, ClassSignature>();
    private ClassSignature clazz;

    public Map<String, ClassSignature> getResult() {
        return this.info;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // System.out.println("\nvisit class:" + name);
        clazz = this.getClassSignature(name);
        ClassSignature parent = this.getClassSignature(superName);

        clazz.setSuper(parent);
        clazz.setFlags(access);
        
        if (interfaces != null) {
            for (String s : interfaces) {
                clazz.addInterface(this.getClassSignature(s));
            }
        }

        if ((access & Opcodes.ACC_INTERFACE) == 1) {
            clazz.setIsInterface();
        }
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        // System.out.println("visit field:" + clazz.getName() + "." + name);
        FieldSignature field = new FieldSignature(name);
        field.setOriginDesc(desc);
        field.setFlags(access);
        field.setType(this.getClassSignature(Type.getType(desc).getClassName()));

        clazz.addField(field);
        return null;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodSignature method = new MethodSignature(name);
        method.setOriginDesc(desc);
        method.setFlags(access);
        method.setReturnType(this.getClassSignature(Type.getReturnType(desc).getClassName()));
        Type[] arguments = Type.getArgumentTypes(desc);
        if (arguments != null) {
            for (Type type : arguments) {
                method.addParam(this.getClassSignature(type.getClassName()));
            }
        }
        if (exceptions != null) {
            for (String exp : exceptions) {
                method.addException(this.getClassSignature(exp));
            }
        }
        clazz.addMethod(method);
        return new MethodSignatureCollector(method);
    }

    private ClassSignature getClassSignature(String name) {
        name = name.replace('/', '.');
        ClassSignature sig = info.get(name);
        if (sig == null) {
            sig = new ClassSignature(name);

            if (name.startsWith("java")) {
                sig.setIgnore(true);
            }

            info.put(name, sig);
        }
        return sig;
    }

    public void visitAttribute(Attribute arg0) {
    }

    public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
        return null;
    }

    public void visitEnd() {
    }

    public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
    }

    public void visitOuterClass(String arg0, String arg1, String arg2) {

    }

    public void visitSource(String arg0, String arg1) {

    }

}
