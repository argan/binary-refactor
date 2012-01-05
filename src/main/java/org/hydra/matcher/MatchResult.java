package org.hydra.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hydra.util.Lists.Pair;

public class MatchResult {

    private Map<String, Set<ClassMatchResult>> map = new HashMap<String, Set<ClassMatchResult>>();

    public Map<String, Set<ClassMatchResult>> getResult() {
        return map;
    }

    public void addClassMatchResult(ClassMatchResult clzz) {
        Set<ClassMatchResult> result = this.map.get(clzz.getLeftName());
        if (result == null) {
            result = new HashSet<ClassMatchResult>();
            map.put(clzz.getLeftName(), result);
        }
        result.add(clzz);
    }

    public static class ClassMatchResult {
        private Pair<String, String> name;
        private List<Pair<FieldMatchResult, FieldMatchResult>> fields = new ArrayList<Pair<FieldMatchResult, FieldMatchResult>>();
        private List<Pair<MethodMatchResult, MethodMatchResult>> methods = new ArrayList<Pair<MethodMatchResult, MethodMatchResult>>();

        ClassMatchResult(String left, String right) {
            this.name = new Pair<String, String>(left, right);
        }

        public String getLeftName() {
            return this.name.getLeft();
        }

        public List<Pair<FieldMatchResult, FieldMatchResult>> getFields() {
            return this.fields;
        }

        public List<Pair<MethodMatchResult, MethodMatchResult>> getMethods() {
            return this.methods;
        }

        public void addField(FieldMatchResult left, FieldMatchResult right) {
            this.fields.add(new Pair<FieldMatchResult, FieldMatchResult>(left, right));
        }

        public void addMethod(MethodMatchResult left, MethodMatchResult right) {
            this.methods.add(new Pair<MethodMatchResult, MethodMatchResult>(left, right));
        }

        public String getRightName() {
            return this.name.getRight();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ClassMatchResult other = (ClassMatchResult) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

    }

    public static class FieldMatchResult {
        private String name, desc;

        public FieldMatchResult(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public String toString() {
            return "Field[" + this.name + "]" + this.desc;
        }
    }

    public static class MethodMatchResult {
        private String name, desc;

        public MethodMatchResult(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public String toString() {
            return "Method[" + this.name + "]" + this.desc;
        }

    }
}
