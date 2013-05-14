package org.hydra.matcher;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.hydra.matcher.MatchResult.ClassMatchResult;
import org.hydra.matcher.MatchResult.FieldMatchResult;
import org.hydra.matcher.MatchResult.MethodMatchResult;
import org.hydra.util.Lists;
import org.hydra.util.Lists.MapFunc;
import org.hydra.util.Lists.Pair;
import org.hydra.util.Lists.Predicate;
import org.hydra.util.Log;
import org.objectweb.asm.ClassReader;

public class BinaryMatcher {

    public static MatchResult match(String jarFile, String jarsDir) {
        File[] jarFiles = new File(jarsDir).listFiles(new FilenameFilter() {

            public boolean accept(File arg0, String arg1) {
                return arg1.matches(".+jar$");
            }
        });

        return match(new File(jarFile), jarFiles);
    }

    public static MatchResult match(File jarFile, File[] libs) {
        try {
            MatchResult matchResult = new MatchResult();
            List<ClassSignature> srcJar = collect(jarFile);
            for (File jar : libs) {
                List<ClassSignature> result = collect(jar);

                // print(result);
                Log.debug("========== " + jar.getName());
                // print(result2);

                find(matchResult, srcJar, result);
            }
            return matchResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<ClassSignature> filter(List<ClassSignature> result) {
        result = Lists.filter(result, new Predicate<ClassSignature>() {

            public boolean apply(ClassSignature t) {
                return !Types.isUnchangableType(t.getName()) && t.getName().indexOf("[") == -1;
            }
        });
        return result;
    }

    /**
     * 找出完全匹配的集合
     * 
     * @param matchResult
     * 
     * @param result
     *            已知的集合
     * @param result2
     *            待分析的集合
     */
    private static void find(MatchResult matchResult, List<ClassSignature> result, List<ClassSignature> result2) {
        List<Pair<String, ClassSignature>> level0Sig = Lists.map(result2,
                new MapFunc<ClassSignature, Lists.Pair<String, ClassSignature>>() {

                    public Pair<String, ClassSignature> apply(ClassSignature in) {
                        return new Pair<String, ClassSignature>(in.getLevel0Sig(), in);
                    }
                });
        int count = 0;
        for (ClassSignature c : result) {
            final String sig = c.getLevel0Sig();
            List<Pair<String, ClassSignature>> matches = Lists.filter(level0Sig,
                    new Predicate<Pair<String, ClassSignature>>() {

                        public boolean apply(Pair<String, ClassSignature> in) {
                            return in.getLeft().equals(sig);
                        }
                    });
            if (matches.size() == 1) {
                count++;
                Log.debug(c.getName() + " = " + matches.get(0).getRight().getName());
                ClassMatchResult clzz = new ClassMatchResult(c.getName(), matches.get(0).getRight().getName());
                matchResult.addClassMatchResult(clzz);
                matchMethodsAndFields(clzz, c, matches.get(0).getRight());
            } else if (matches.size() > 1) {
                // TODO what's the matter?
                Log.error("TODO : dumplicated matches found for %s", c.getName());
            }
        }
        Log.debug("Match count:%d ", count);
    }

    /**
     * 将method等的对应关系找出来
     * 
     * @param clzz
     * 
     * @param c
     * @param pair
     */
    private static void matchMethodsAndFields(ClassMatchResult clzz, ClassSignature c, ClassSignature pair) {
        for (int i = 0; i < c.getFields().size(); i++) {
            FieldSignature f1 = c.getFields().get(i);
            FieldSignature f2 = pair.getFields().get(i);
            assert f1.getOriginDesc() == f2.getOriginDesc();
            Log.debug("\tf " + f1.getName() + " " + f1.getOriginDesc() + " = " + f2.getName() + " "
                    + f2.getOriginDesc());
            clzz.addField(new FieldMatchResult(f1.getName(), f1.getOriginDesc()),
                    new FieldMatchResult(f2.getName(), f2.getOriginDesc()));
        }
        for (int i = 0; i < c.getMethods().size(); i++) {
            MethodSignature m1 = c.getMethods().get(i);
            MethodSignature m2 = pair.getMethods().get(i);
            assert m1.getOriginDesc() == m2.getOriginDesc();
            Log.debug("\tm " + m1.getName() + " " + m1.getOriginDesc() + " = " + m2.getName());
            clzz.addMethod(new MethodMatchResult(m1.getName(), m1.getOriginDesc()), new MethodMatchResult(m2.getName(),
                    m2.getOriginDesc()));
        }
    }

    private static List<ClassSignature> collect(File jar) throws IOException {
        ClassSignatureCollector extractor = new ClassSignatureCollector();
        JarFile jarFile = new JarFile(jar);
        for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                ClassReader reader = new ClassReader(jarFile.getInputStream(entry));
                reader.accept(extractor, 0);
            }
        }
        jarFile.close();
        // get the result
        Map<String, ClassSignature> result = extractor.getResult();
        return filter(new ArrayList<ClassSignature>(result.values()));
    }
}
