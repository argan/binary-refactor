package org.hydra.renamer.asm;

import org.hydra.util.Log;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

public class ClassForNameFixMethodVistor extends MethodAdapter {

    private Remapper remapper;

    public ClassForNameFixMethodVistor(MethodVisitor mv, Remapper remapper) {
        super(mv);
        this.remapper = remapper;
    }

    @Override
    public void visitLdcInsn(Object cst) {
        if (cst instanceof String && cst != null) {
            // 如果是装载一个常量的字符串，检查在不在我们的类名列表里，如果是，就修改掉
            String oldName = ((String) cst).replace('.', '/');
            // Log.debug("visitLdcInsn %s", oldName);
            String newName = remapper.map(oldName);
            if (newName != null && !oldName.equals(newName)) {
                newName = newName.replace('/', '.');
                Log.debug("change constant loading from %s to %s", cst, newName);
                super.visitLdcInsn(newName);
                return;
            }
        }
        super.visitLdcInsn(cst);
    }

}
