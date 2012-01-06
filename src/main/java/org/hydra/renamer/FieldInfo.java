package org.hydra.renamer;

import org.springframework.asm.Type;

public class FieldInfo {

    private String name;
    private String desc;
    private ClassInfo enclosedClass;

    public FieldInfo(String name, String desc) {
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

    public String getType() {
        return Type.getType(desc).getClassName();
    }
}
