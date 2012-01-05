package org.hydra.matcher;

import java.util.Map;
import java.util.Set;

import org.hydra.matcher.MatchResult.ClassMatchResult;
import org.hydra.matcher.MatchResult.FieldMatchResult;
import org.hydra.matcher.MatchResult.MethodMatchResult;
import org.hydra.util.Lists.Pair;
import org.hydra.util.Log;

public class Main {
    public static void main(String[] args) {
        String dir = "/Users/argan/Opensource/myown/binary-refactor/jars";
        String jar2 = "/Users/argan/crack/jrebel-4.0/jrebel/jrebel.jar";

        MatchResult result = BinaryMatcher.match(jar2, dir);

        Log.debug("Match count: %d ", result.getResult().size());

        for (Map.Entry<String, Set<ClassMatchResult>> c : result.getResult().entrySet()) {
            Log.debug("=== matches for %s ===", c.getKey());
            for (ClassMatchResult cc : c.getValue()) {
                Log.debug("  class : %s = %s", cc.getLeftName(), cc.getRightName());
                for (Pair<FieldMatchResult, FieldMatchResult> fp : cc.getFields()) {
                    Log.debug("    F : %s = %s ", fp.getLeft(), fp.getRight());
                }
                for (Pair<MethodMatchResult, MethodMatchResult> fp : cc.getMethods()) {
                    Log.debug("    M : %s = %s ", fp.getLeft(), fp.getRight());
                }
            }
        }
    }
}
