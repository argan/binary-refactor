package org.hydra.renamer;

public class Pair {
    private String left;
    private String right;

    public Pair(String name, String signature) {
        super();
        this.left = name;
        this.right = signature;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "Pair [left=" + left + ", right=" + right + "]";
    }

}
