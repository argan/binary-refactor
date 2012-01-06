package org.hydra.renamer;

import org.objectweb.asm.Type;

public class MethodInfo {
    private String name;
    private String desc;
    private ClassInfo enclosedClass;

    public MethodInfo(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public void setEnclosedClass(ClassInfo enclosedClass) {
        this.enclosedClass = enclosedClass;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public ClassInfo getEnclosedClass() {
        return enclosedClass;
    }

    public String getReturnType() {
        return Type.getReturnType(desc).getClassName();
    }

    public String[] getParameterTypes() {
        Type[] types = Type.getArgumentTypes(desc);

        if (types == null) {
            return null;
        }

        String[] result = new String[types.length];

        for (int i = 0; i < types.length; i++) {
            result[i] = types[i].getClassName();
        }
        return result;

    }
}
