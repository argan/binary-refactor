package org.hydra.matcher;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class MethodSignatureCollector implements MethodVisitor {
    private MethodSignature methodSig;

    public MethodSignatureCollector(MethodSignature methodSig) {
        this.methodSig = methodSig;
    }

    public AnnotationVisitor visitAnnotationDefault() {
        // TODO annotation visitor
        return null;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        // TODO annotation visitor
        return null;
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        // TODO annotation visitor
        return null;
    }

    public void visitAttribute(Attribute attr) {
        // TODO attribute visitor?

    }

    public void visitCode() {

    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {

    }

    public void visitInsn(int opcode) {
        this.methodSig.addInsn(opcode);
    }

    public void visitIntInsn(int opcode, int operand) {
        this.methodSig.addInsn(opcode);
    }

    public void visitVarInsn(int opcode, int var) {
        this.methodSig.addInsn(opcode);
    }

    public void visitTypeInsn(int opcode, String type) {
        this.methodSig.addInsn(opcode);
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.methodSig.addInsn(opcode);
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.methodSig.addInsn(opcode);
    }

    public void visitJumpInsn(int opcode, Label label) {
        this.methodSig.addInsn(opcode);
    }

    public void visitLabel(Label label) {
    }

    public void visitLdcInsn(Object cst) {

    }

    public void visitIincInsn(int var, int increment) {

    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {

    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {

    }

    public void visitMultiANewArrayInsn(String desc, int dims) {

    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {

    }

    public void visitLocalVariable(String name, String desc, String signature, Label start,
                                   Label end, int index) {

    }

    public void visitLineNumber(int line, Label start) {

    }

    public void visitMaxs(int maxStack, int maxLocals) {

    }

    public void visitEnd() {

    }

}
