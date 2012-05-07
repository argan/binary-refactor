package org.hydra.renamer;

import java.lang.reflect.Modifier;

public abstract class AccessableNode {

    private int access;

    public AccessableNode(int access) {
        this.access = access;
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
