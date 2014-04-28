package org.hydra.renamer;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.hydra.util.Lists;

public abstract class AccessableNode {

    private int       access;
    private ClassInfo enclosedClass;

    public AccessableNode(int access) {
        this.access = access;
    }

    public void setEnclosedClass(ClassInfo enclosedClass) {
        this.enclosedClass = enclosedClass;
    }

    public ClassInfo getEnclosedClass() {
        return enclosedClass;
    }

    public List<String> getFlags() {
        List<String> flags = new ArrayList<String>();
        if (isPublic()) {
            flags.add("public");
        }
        if (isAbstract()) {
            flags.add("abstract");
        }
        if (isStatic()) {
            flags.add("static");
        }
        if (isFinal()) {
            flags.add("final");
        }
        return flags;
    }

    public String getFlagString() {
        return Lists.mkString(getFlags());
    }

    public boolean isPublic() {
        return Modifier.isPublic(this.access);
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(this.access);
    }

    public boolean isInterface() {
        return Modifier.isInterface(this.access);
    }

    public boolean isStatic() {
        return Modifier.isStatic(this.access);
    }

    public boolean isFinal() {
        return Modifier.isFinal(this.access);
    }
}
