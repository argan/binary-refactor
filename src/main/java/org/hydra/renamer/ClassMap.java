package org.hydra.renamer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hydra.renamer.RenameConfig.ClassRenameInfo;
import org.hydra.utils.Log;
import org.hydra.utils.Utils;

public class ClassMap {
    private Map<String, ClassInfo> map = new HashMap<String, ClassInfo>();

    public ClassInfo getClassInfo(String name) {
        return this.map.get(name);
    }

    public void addClassInfo(ClassInfo info) {
        this.map.put(info.getClassName(), info);
    }

    public void rebuildConfig(RenameConfig config, String addPkgs) {
        // build the hierarchy and rebuild the Config
        for (Map.Entry<String, ClassInfo> entry : map.entrySet()) {
            ClassInfo info = entry.getValue();
            if (info.getSuperClass() != null) {
                info.getSuperClass().getChildren().add(info);
            }
            for (ClassInfo intf : info.getInterfaces()) {
                intf.getChildren().add(info);
            }
        }
        List<String> addPkgsList = Arrays.asList(Utils.tokens(addPkgs));
        if (addPkgsList != null) {
            for (String name : map.keySet()) {
                if (!config.getConfig().containsKey(name)) {
                    config.getConfig().put(name, new ClassRenameInfo());
                }
            }
        }
        // 重建 config ，主要是根据类／接口的层次结构修改
        for (Map.Entry<String, ClassRenameInfo> entry : config.getConfig().entrySet()) {
            String clazz = entry.getKey();
            ClassInfo classInfo = map.get(clazz);
            if (classInfo == null) {
                Log.debug("classInfo not found for %s", clazz);
            } else {
                entry.getValue().setSuperClassName(
                        classInfo.getSuperClass() == null ? null : classInfo.getSuperClass().getClassName());
                if (classInfo.getInterfaces().size() > 0) {
                    for (ClassInfo i : classInfo.getInterfaces()) {
                        entry.getValue().addInterface(i.getClassName());
                    }
                }
            }
        }
    }

    public String toString() {
        return String.format("ClassMap {%s}", map);
    }
}
