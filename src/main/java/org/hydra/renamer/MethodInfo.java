package org.hydra.renamer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.Type;

public class MethodInfo extends AccessableNode implements Comparable<MethodInfo> {
    private String name;
    private String desc;
    private String[] exceptions;

    // dependencies in code and signature
    private Set<String> dependencies = new HashSet<String>();

    public MethodInfo(String name, String desc, int access, String[] exceptions) {
        super(access);
        this.name = name;
        this.desc = desc;
        this.exceptions = exceptions;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
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

    public String[] getExceptions() {
        return this.exceptions;
    }

    public Set<String> getDependencies() {
        return Collections.unmodifiableSet(this.dependencies);
    }

    public void addDependency(String type) {
        this.dependencies.add(type);
    }

    public void setExceptions(String[] exceptions) {
        if (exceptions != null) {
            for (String exp : exceptions) {
                this.addDependency(exp);
            }
        }
    }

    @Override
    public int compareTo(MethodInfo o) {
        int diffDesc = this.desc.compareTo(o.desc);
        return diffDesc == 0 ? this.name.compareTo(o.name) : diffDesc;
    }
}
