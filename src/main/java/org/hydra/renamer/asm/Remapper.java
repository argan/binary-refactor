package org.hydra.renamer.asm;

import org.hydra.renamer.RenameConfig;
import org.hydra.util.Log;

public class Remapper extends org.objectweb.asm.commons.Remapper {
    private RenameConfig config;

    public Remapper(RenameConfig config) {
        this.config = config;
    }

    @Override
    public String map(String typeName) {
        String newName = this.config.getClassNewName(typeName);
        if (typeName.equals(newName) == false) {
            Log.debug("rename %s to %s", typeName, newName);
        }
        return newName;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        String newName = this.config.getFieldNewName(owner, name, desc);
        if (name.equals(newName) == false) {
            Log.debug("rename field %s in %s to %s", name, owner, newName);
        }
        return newName;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        String newName = this.config.getMethodNewName(owner, name, desc);
        if (name.equals(newName) == false) {
            Log.debug("rename method %s in %s to %s", name, owner, newName);
        }
        return newName;
    }

}
