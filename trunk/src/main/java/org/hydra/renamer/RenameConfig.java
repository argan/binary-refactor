package org.hydra.renamer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameConfig {
    private Map<String, ClassRenameInfo> config = new HashMap<String, ClassRenameInfo>();

    public RenameConfig(String file) {
        parse(file);
    }

    public Map<String, ClassRenameInfo> getConfig() {
        return this.config;
    }

    private boolean hasClass(String name) {
        return this.config.keySet().contains(name);
    }

    public String getClassNewName(String name) {
        if (this.hasClass(name) && this.config.get(name).classInfo != null) {
            String newName = this.config.get(name).classInfo.getRight();
            // Log.debug("rename class from %s to %s", name, newName);
            if (newName != null) {
                return newName;
            }
        }
        return name;
    }

    public String getFieldNewName(String clazzName, String name, String type) {
        String newName = getNewName(clazzName, name, type, false);
        return newName == null ? name : newName;
    }

    public String getMethodNewName(String clazzName, String name, String desc) {
        String newName = getNewName(clazzName, name, desc, true);
        return newName == null ? name : newName;
    }

    private String getNewName(String clazzName, String name, String desc, boolean isMethod) {
        if (this.hasClass(clazzName)) {
            ClassRenameInfo classRenameInfo = this.config.get(clazzName);
            if (isMethod && "d".equals(name) && clazzName.equals("com/zeroturnaround/javarebel/hO")) {
                Log.debug("hello");
            }
            String newName;
            // 检查自己
            if (isMethod) {
                newName = classRenameInfo.methodInfo.get(conbineKey(name, desc));
            } else {
                newName = classRenameInfo.fieldInfo.get(conbineKey(name, desc));
            }
            if (newName != null) {
                return newName;
            }
            // 检查基类
            if (classRenameInfo.superClassName != null) {
                newName = getNewName(classRenameInfo.superClassName, name, desc, isMethod);
                if (newName != null) {
                    return newName;
                }
            }
            // 检查接口里
            if (classRenameInfo.interfaces.size() > 0) {
                for (String interName : classRenameInfo.interfaces) {
                    newName = getNewName(interName, name, desc, isMethod);
                    if (newName != null) {
                        return newName;
                    }
                }
            }

        }
        return null;
    }

    private void addClass(String oldName, String newName) {
        ClassRenameInfo info = this.config.get(oldName);
        if (info == null) {
            info = new ClassRenameInfo();
            info.setClassInfo(new Pair(oldName, newName));
            this.config.put(oldName, info);
        } else {
            Log.error("config error? %s already exists", oldName);
        }
    }

    private void addField(String clazzName, String fieldName, String fieldType, String newName) {
        ClassRenameInfo info = this.config.get(clazzName);
        if (info == null) {
            Log.error("config error? %s not exists", clazzName);
        } else {
            info.getFieldInfo().put(conbineKey(fieldName, fieldType), newName);
        }
    }

    private void addMethod(String clazzName, String methodName, String methodDesc, String newName) {
        ClassRenameInfo info = this.config.get(clazzName);
        if (info == null) {
            Log.error("config error? %s not exists", clazzName);
        } else {
            info.getMethodInfo().put(conbineKey(methodName, methodDesc), newName);
        }
    }

    private String conbineKey(String methodName, String methodDesc) {
        return methodName + ":" + methodDesc;
    }

    private void parse(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            String currentClass = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0)
                    continue;
                String[] tokens = Utils.tokens(line);
                if (tokens != null && tokens.length > 3) {
                    /**
                     * <pre>
                     * class:  org.hydra.pkg.OldClassName to org.hydra.pkg.NewClassName
                     * method: oldMethodName (IILjava/lang/String;)Ljava/lang/Integer; to newClassName
                     * field:  oldFieldName Ljava/lang/String; to newFieldName
                     * </pre>
                     */
                    if ("class:".equalsIgnoreCase(tokens[0])) {
                        currentClass = tokens[1];
                        this.addClass(currentClass, tokens[3]);
                    } else if ("method:".equalsIgnoreCase(tokens[0]) && tokens.length > 4) {
                        this.addMethod(currentClass, tokens[1], tokens[2], tokens[4]);
                    } else if ("field:".equalsIgnoreCase(tokens[0]) && tokens.length > 4) {
                        this.addField(currentClass, tokens[1], tokens[2], tokens[4]);
                    } else if (!line.startsWith("#")) {
                        Log.error("error config line %s", line);
                    }
                } else if (!line.startsWith("#")) {
                    Log.error("error config line %s", line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClassRenameInfo {
        private String superClassName;
        private List<String> interfaces;

        private Pair classInfo;
        /**
         * Key:格式［fieldName:fieldType] Value:newName
         */
        private Map<String, String> fieldInfo;
        /**
         * Key:格式［methodName:fieldDesc]
         */
        private Map<String, String> methodInfo;

        public ClassRenameInfo() {
            this.fieldInfo = new HashMap<String, String>();
            this.methodInfo = new HashMap<String, String>();
            this.interfaces = new ArrayList<String>();
        }

        public void setSuperClassName(String superClassName) {
            this.superClassName = superClassName;
        }

        public void addInterface(String in) {
            this.interfaces.add(in);
        }

        public String toString() {
            return String.format(
                    "ClassRenameInfo %s ,superClass %s,Interfaces %s ,\n fieldInfo:%s,\n methodInfo:%s \n", classInfo,
                    superClassName, interfaces, fieldInfo, methodInfo);
        }

        public Pair getClassInfo() {
            return classInfo;
        }

        public void setClassInfo(Pair classInfo) {
            this.classInfo = classInfo;
        }

        public Map<String, String> getFieldInfo() {
            return fieldInfo;
        }

        public Map<String, String> getMethodInfo() {
            return methodInfo;
        }

    }
}
