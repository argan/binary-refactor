package org.hydra.gui.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.hydra.util.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/jarviewer")
public class UploadController {

    @RequestMapping(value = "form", method = RequestMethod.GET)
    public void form() {
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public void list(Model model) {
        model.addAttribute("list", Database.list("file"));
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String delete(@RequestParam("id") String jarId) {
        Database.delete(jarId);
        return "redirect:list.htm";
    }
    
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(Model model, @RequestParam("file") MultipartFile file) {
        String id = Database.Util.nextId();
        File f = new File(Database.getConfig().getUploadPath(), id + ".upload");
        OutputStream os = null;
        try {
            os = new FileOutputStream(f);
            os.write(file.getBytes());
            os.flush();
            FileItem item = new FileItem(f.getCanonicalPath(), file.getOriginalFilename(), file.getContentType());
            Database.save("file", id, item);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.close(os);
        }
        return "redirect:list.htm";
    }

}
