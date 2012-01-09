package org.hydra.renamer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hydra.util.Lists.Pair;

public class ClassInfo {
    private ClassMap map;
    // internalName like java/lang/String
    private String className;
    // direct superClass
    private String superClassName;
    // all interfaces
    private List<String> interfaceNames = new ArrayList<String>();
    // direct children
    private List<String> childrenNames = new ArrayList<String>();

    private List<MethodInfo> methods = new ArrayList<MethodInfo>(0);
    private List<FieldInfo> fields = new ArrayList<FieldInfo>(0);

    private boolean isInterface;

    public boolean isInterface() {
        return isInterface;
    }

    public ClassInfo(ClassMap map, String name, boolean isInterface) {
        this.map = map;
        this.className = name;
        this.isInterface = isInterface;
    }

    public void setSuperClass(String superClass) {
        this.superClassName = superClass;
    }

    public String getClassName() {
        return className;
    }

    public String getClassShortName() {
        int index = className.lastIndexOf("/");

        return className.substring(index + 1);
    }

    public ClassInfo getSuperClass() {
        return this.map.getClassInfo(this.superClassName);
    }

    public List<ClassInfo> getInterfaces() {
        return str2info(this.interfaceNames);
    }

    private List<ClassInfo> str2info(List<String> names) {
        List<ClassInfo> info = new ArrayList<ClassInfo>(names.size());
        for (String s : names) {
            if (this.map.getClassInfo(s) != null) {
                info.add(this.map.getClassInfo(s));
            }
        }
        return info;
    }

    public List<ClassInfo> getChildren() {
        return str2info(this.childrenNames);
    }

    public List<MethodInfo> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    public List<FieldInfo> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public boolean hasMethod(String name, String desc) {
        return this.methods.contains(new Pair<String, String>(name, desc));
    }

    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("ClassInfo{").append(isInterface ? "interface" : "class").append(":").append(this.className);
        if (this.superClassName != null) {
            buff.append(",super:").append(superClassName);
        }
        if (this.interfaceNames.size() > 0) {
            if (isInterface) {
                buff.append(",superInterfaces[");
            } else {
                buff.append(",interfaces[");
            }
            for (String info : this.interfaceNames) {
                buff.append(info).append(",");
            }
            buff.append("]");
        }
        if (this.fields.size() > 0) {
            buff.append(",fields:[");
            for (FieldInfo p : this.fields) {
                buff.append(p.getName()).append("/").append(p.getDesc()).append(",");
            }
            buff.append("]");
        }
        if (this.methods.size() > 0) {
            buff.append(",methods:[");
            for (MethodInfo p : this.methods) {
                buff.append(p.getName()).append("/").append(p.getDesc()).append(",");
            }
            buff.append("]");
        }
        buff.append("}");
        return buff.toString();
    }

    public void addMethod(MethodInfo methodInfo) {
        this.methods.add(methodInfo);
        methodInfo.setEnclosedClass(this);
    }

    public void addField(FieldInfo fieldInfo) {
        this.fields.add(fieldInfo);
        fieldInfo.setEnclosedClass(this);
    }

    public void addInterface(String classInfo) {
        this.interfaceNames.add(classInfo);
    }

    public void addChild(String info) {
        this.childrenNames.add(info);
    }
}
