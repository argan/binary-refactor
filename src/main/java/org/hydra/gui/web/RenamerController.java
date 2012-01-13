package org.hydra.gui.web;

import java.io.File;
import java.io.IOException;

import org.hydra.renamer.RenameConfig;
import org.hydra.renamer.Renamer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/renamer")
public class RenamerController {

    @RequestMapping("/edit")
    public String editClass(Model model, @RequestParam("id") String jar, @RequestParam("className") String clazzName,
            @RequestParam("newClassName") String newClassName, @RequestParam("renameConfig") String renameConfig) {
        model.addAttribute("clzName", newClassName);
        model.addAttribute("clz", newClassName);
        model.addAttribute("id", jar);

        if (Database.get(jar) == null) {
            return "/jarviewer/view";
        }

        renameConfig = renameConfig.trim();
        renameConfig = "class: " + clazzName + " to " + newClassName + "\n" + renameConfig;

        RenameConfig config = RenameConfig.loadFromString(renameConfig);

        FileItem path = (FileItem) Database.get(jar).getObj();

        String oldjar = path.getFullName();
        String newjar = jar + "_" + path.getVersion();

        File file = new File(Database.getConfig().getUploadPath(), newjar + ".upload");

        String filePath = null;
        try {
            filePath = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Renamer.rename(config, oldjar, filePath);
        path.setFulleName(filePath);
        Database.save("file", jar, path);
        return "redirect:/jarviewer/view.htm";//?id=" + jar + "&clz=" + newClassName;
    }
}
