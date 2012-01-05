package org.hydra.gui.web.jarviewer;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import org.hydra.gui.web.Database;
import org.hydra.renamer.ClassMap;
import org.hydra.renamer.RenameConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "jarviewer")
public class ViewController {

    @RequestMapping(value = "view")
    public void view(Model model, @RequestParam("id") String jar,
            @RequestParam(value = "clz", required = false) String clazzName) {
        String path = (String) Database.get(jar).getObj();
        try {
            ClassMap classMap = ClassMap.build(new JarFile(new File(Database.getConfig().getUploadPath(), jar
                    + ".upload").getCanonicalPath()));

            classMap.rebuildConfig(new RenameConfig(), null);
            model.addAttribute("clzName", clazzName);
            model.addAttribute("id", jar);
            model.addAttribute("classMap", classMap);
            model.addAttribute("origName", path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
