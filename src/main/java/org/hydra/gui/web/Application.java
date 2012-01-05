package org.hydra.gui.web;

import org.hydra.matcher.BinaryMatcher;
import org.hydra.matcher.MatchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Application {

    @RequestMapping(value = "/index")
    public void index(Model model) {
        String dir = "/Users/argan/Opensource/myown/binary-refactor/jars";
        String jar2 = "/Users/argan/crack/jrebel-4.0/jrebel/jrebel.jar";

        MatchResult result = BinaryMatcher.match(jar2, dir);

        model.addAttribute("date", new java.util.Date());
        model.addAttribute("result", result);
        model.addAttribute("jarfile",jar2);
    }
}
