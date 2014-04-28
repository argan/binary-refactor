package org.hydra.matcher;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.hydra.util.Lists;
import org.hydra.util.Lists.AccFunc;

/**
 * 用类结构来表示某一个类，忽略名字信息，只关注类型信息
 * 
 * @author argan
 * 
 */
public class ClassSignature {
    public ClassSignature(String name2) {
        this.name = name2;
    }

    private int                   flags;
    private String                name;
    private List<FieldSignature>  fields      = new ArrayList<FieldSignature>();
    private List<MethodSignature> methods     = new ArrayList<MethodSignature>();
    private ClassSignature        superClass;
    private List<ClassSignature>  interfaces  = new ArrayList<ClassSignature>();
    private boolean               isInterface = false;
    private boolean               isAbstract  = false;

    public void setSuper(ClassSignature parent) {
        this.superClass = parent;
    }

    public void addInterface(ClassSignature p) {
        this.interfaces.add(p);
    }

    public void setIsInterface() {
        this.isInterface = true;
    }

    public void addField(FieldSignature field) {
        field.setIgnore(this.ignore);
        this.fields.add(field);
    }

    /**
     * 计算粗略的特征信息，不涉及类型，只关心数量，不递归
     */
    public String getLevel0Sig() {
        StringBuilder sb = new StringBuilder();
        sb.append(isInterface).append("|");
        sb.append(isAbstract).append("|");

        if (interfaces.size() > 0) {
            sb.append("I{")
                .append(Lists.acc(this.interfaces, "", new AccFunc<ClassSignature, String>() {

                    public String apply(ClassSignature in, String out) {
                        return out + Types.qualify(in.getName()) + ",";
                    }
                })).append("}");
        }
        if (fields.size() > 0) {
            sb.append("F{");
            for (FieldSignature ms : fields) {
                sb.append(ms.getLevel0Sig()).append("|");
            }
            sb.append("}");
        }

        if (methods.size() > 0) {
            sb.append("M{");
            for (MethodSignature ms : methods) {
                sb.append(ms.getLevel0Sig()).append("|");
            }
            sb.append("}");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.getLevel0Sig();
    }

    private boolean ignore = false;

    public boolean isIgnore() {
        return this.ignore;
    }

    public void setIgnore(boolean b) {
        this.ignore = b;
    }

    public void addMethod(MethodSignature method) {
        method.setIgnore(this.ignore);
        this.methods.add(method);
    }

    public String getName() {
        return this.name;
    }

    public List<FieldSignature> getFields() {
        return fields;
    }

    public List<MethodSignature> getMethods() {
        return methods;
    }

    public ClassSignature getSuperClass() {
        return superClass;
    }

    public List<ClassSignature> getInterfaces() {
        return interfaces;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setFlags(int access) {
        this.flags = access;
        this.isAbstract = Modifier.isAbstract(this.flags);
    }

}
