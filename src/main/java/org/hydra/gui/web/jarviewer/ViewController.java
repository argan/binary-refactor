package org.hydra.gui.web.jarviewer;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import org.hydra.gui.web.Database;
import org.hydra.gui.web.FileItem;
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
        model.addAttribute("clzName", clazzName);
        model.addAttribute("id", jar);

        if (Database.get(jar) == null) {
            return;
        }

        FileItem path = (FileItem) Database.get(jar).getObj();
        try {
            ClassMap classMap = ClassMap.build(new JarFile(new File(path.getFulleName()).getCanonicalPath()));

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
}
