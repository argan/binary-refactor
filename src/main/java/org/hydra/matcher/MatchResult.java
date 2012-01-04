package org.hydra.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hydra.util.Lists.Pair;

public class MatchResult {

    private Map<String, List<ClassMatchResult>> map = new HashMap<String, List<ClassMatchResult>>();

    public Map<String, List<ClassMatchResult>> getResult() {
        return map;
    }

    public void addClassMatchResult(ClassMatchResult clzz) {
        List<ClassMatchResult> result = this.map.get(clzz.getLeftName());
        if (result == null) {
            result = new ArrayList<ClassMatchResult>();
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

        String getLeftName() {
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
