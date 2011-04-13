package org.hydra.renamer;

public class Remapper extends org.objectweb.asm.commons.Remapper {
    private RenameConfig config;

    public Remapper(RenameConfig config) {
        this.config = config;
    }

    @Override
    public String map(String typeName) {
        return this.config.getClassNewName(typeName);
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        return this.config.getFieldNewName(owner, name, desc);
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        return this.config.getMethodNewName(owner, name, desc);
    }

}
