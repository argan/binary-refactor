package org.hydra.gui.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hydra.renamer.ClassInfo;
import org.hydra.renamer.ClassMap;
import org.hydra.renamer.ClassMap.ClassWalker;
import org.hydra.renamer.RenameConfig;
import org.hydra.util.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifierClassVisitor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

@Controller
@RequestMapping("/jarviewer")
public class ViewController {

    @RequestMapping("/view")
    public void view(Model model, @RequestParam("id") String jar,
            @RequestParam(value = "clz", required = false) String clazzName) {

        clazzName = clazzName != null ? clazzName.replace('.', '/') : null;

        model.addAttribute("clzName", clazzName);
        model.addAttribute("id", jar);

        if (Database.get(jar) == null) {
            return;
        }

        FileItem path = (FileItem) Database.get(jar).getObj();
        try {
            ClassMap classMap = ClassMap.build(new JarFile(new File(path.getFullName()).getCanonicalPath()));

            classMap.rebuildConfig(new RenameConfig(), null);

            model.addAttribute("classMap", classMap);
            model.addAttribute("origName", path.getOrigName());

            // to find which package should open
            if (clazzName != null) {
                model.addAttribute("openPkg", classMap.getShortPackage(clazzName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/graph")
    public void graph(Model model, @RequestParam("id") String jar,
            @RequestParam(value = "clz", required = false) String clz,
            @RequestParam(value = "type", required = false) final String type) {
        model.addAttribute("id", jar);
        model.addAttribute("clz", clz);
        model.addAttribute("type", type);
    }

    @RequestMapping("/graphdata")
    public View graphdata(@RequestParam("id") final String jar,
            @RequestParam(value = "clz", required = false) final String clz,
            @RequestParam(value = "type", required = false) final String type) {
        FileItem path = (FileItem) Database.get(jar).getObj();
        try {
            final ClassMap classMap = ClassMap.build(new JarFile(new File(path.getFullName()).getCanonicalPath()));
            classMap.rebuildConfig(new RenameConfig(), null);
            return new View() {

                @Override
                public String getContentType() {
                    return "application/json";
                    // return "text/plain";
                }

                @Override
                public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
                        throws Exception {
                    BufferedWriter writer = new BufferedWriter(response.getWriter());
                    Map<String, List<ClassInfo>> tree = classMap.getTree();
                    int count = 0;
                    Set<String> allObj = new HashSet<String>();
                    writer.write("{\"src\":\"");
                    ClassInfo clzInfo = classMap.getClassInfo(clz);
                    if (clzInfo != null) {
                        // partial dep-graph
                        if ("mydeps".equals(type)) {
                            // deps of clz
                            for (String dep : clzInfo.getDependencies()) {
                                if (classMap.contains(dep)) {
                                    writeDep(writer, clz, dep);
                                }
                            }
                        } else if ("depsonme".equals(type)) {
                            // who depends on me ?
                            for (Map.Entry<String, List<ClassInfo>> e : tree.entrySet()) {
                                for (ClassInfo info : e.getValue()) {
                                    if (info.getDependencies().contains(clz)) {
                                        writeDep(writer, info.getClassName(), clz);
                                    }
                                }
                            }
                        }
                        writer.write(clz + " {color:red,link:'/'}");
                    } else {
                        // all data
                        for (Map.Entry<String, List<ClassInfo>> e : tree.entrySet()) {
                            writer.write(";" + e.getKey() + "\\n");
                            for (ClassInfo clazz : e.getValue()) {
                                for (String dep : clazz.getDependencies()) {
                                    if (classMap.contains(dep)) {
                                        allObj.add(clazz.getClassName());
                                        allObj.add(dep);
                                        writeDep(writer, clazz.getClassShortName(), Utils.getShortName(dep));
                                        count++;
                                    }
                                }
                            }
                            writer.write(";// end of package " + e.getKey() + "\\n");
                        }
                    }
                    writer.write("; totally " + count + " edges.\\n");
                    // TODO link doesn't work,why?
                    // for (String clz : allObj) {
                    // writer.write(Utils.getShortName(clz) +
                    // " {link:'view.htm?id=" + jar + "&clz=" + clz + "'}\\n");
                    // }
                    writer.write("\"}\n");
                    writer.flush();
                    Utils.close(writer);
                }

                private void writeDep(Writer writer, final String src, String dest) throws IOException {
                    writer.write(src);
                    writer.write(" -> ");
                    writer.write(dest);
                    writer.write("\\n");
                }

            };
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/asmdump")
    public void asmdump(Model model, @RequestParam("id") String jarid, @RequestParam("clz") String clazzName) {
        model.addAttribute("clzName", clazzName);
        model.addAttribute("id", jarid);

        if (Database.get(jarid) == null) {
            return;
        }

        FileItem path = (FileItem) Database.get(jarid).getObj();
        model.addAttribute("jarFile", path);
        StringWriter writer = new StringWriter();
        try {
            JarFile jarFile = new JarFile(path.getFullName());
            JarEntry entry = jarFile.getJarEntry(clazzName);
            ClassReader reader = new ClassReader(jarFile.getInputStream(entry));
            reader.accept(new ASMifierClassVisitor(new PrintWriter(writer)), 0);
            model.addAttribute("code", writer.toString().trim());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "download")
    public View download(@RequestParam("id") String jarid) {
        FileItem path = (FileItem) Database.get(jarid).getObj();
        StreamView view = null;
        try {
            File file = new File(path.getFullName());
            view = new StreamView("application/x-jar", new FileInputStream(file));
            view.setBufferSize(4 * 1024);
            view.setContentDisposition("attachment; filename=\"" + path.getOrigName() + "\"");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return view;
    }

    @RequestMapping(value = "search")
    public String search(Model model, @RequestParam("id") String jarid, @RequestParam("q") final String query) {
        model.addAttribute("id", jarid);
        model.addAttribute("query", query);

        if (Database.get(jarid) == null) {
            return null;
        }

        FileItem path = (FileItem) Database.get(jarid).getObj();
        model.addAttribute("jarFile", path);
        try {
            ClassMap classMap = ClassMap.build(new JarFile(new File(path.getFullName()).getCanonicalPath()));

            classMap.rebuildConfig(new RenameConfig(), null);

            model.addAttribute("classMap", classMap);

            final Set<String> matches = new TreeSet<String>();
            ClassWalker walker = new ClassWalker() {

                @Override
                public void walk(ClassInfo classInfo) {
                    if (classInfo.getClassShortName().equals(query)) {
                        matches.add(classInfo.getClassName());
                    }
                }
            };

            classMap.walk(walker);

            if (matches.size() == 1) {
                model.addAttribute("clz", matches.iterator().next());
                return "redirect:/jarviewer/view.htm";
            }
            model.addAttribute("matches", matches);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/jarviewer/search";
    }

}
