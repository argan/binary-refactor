package org.hydra.gui.web;

import java.io.File;

import org.hydra.gui.web.Database.Record;
import org.hydra.matcher.BinaryMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/matcher")
public class MatcherController {

    @RequestMapping("/result")
    public void scan(Model model, @RequestParam("fromJar") String fromJarId,
                     @RequestParam("libJars") String[] libjarids) {

        File fromJar = null;
        Record rec = Database.get(fromJarId);
        if (rec != null) {
            FileItem fromJarItem = (FileItem) (rec.getObj());
            if (fromJarItem != null) {
                model.addAttribute("fromJar", fromJarItem);
                model.addAttribute("fromJarId", fromJarId);
                fromJar = new File(fromJarItem.getFullName());
            }
        }

        File[] libJars = new File[libjarids.length];
        FileItem[] libJarItems = new FileItem[libjarids.length];

        for (int i = 0; i < libjarids.length; i++) {
            FileItem item = (FileItem) Database.get(libjarids[i]).getObj();
            libJarItems[i] = item;
            libJars[i] = new File(item.getFullName());
        }

        model.addAttribute("libJars", libJarItems);

        model.addAttribute("matchResult", BinaryMatcher.match(fromJar, libJars));
    }
}
