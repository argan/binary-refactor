package org.hydra.renamer;

import org.springframework.asm.Type;

public class FieldInfo extends AccessableNode implements Comparable<FieldInfo> {

    private String name;
    private String desc;

    public FieldInfo(String name, String desc, int access) {
        super(access);
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return Type.getType(desc).getClassName();
    }

    @Override
    public int compareTo(FieldInfo o) {

        int diffDesc = this.desc.compareTo(o.desc);
        return diffDesc == 0 ? this.name.compareTo(o.name) : diffDesc;
    }
}
