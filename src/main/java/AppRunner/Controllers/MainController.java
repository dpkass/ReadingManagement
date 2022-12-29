package AppRunner.Controllers;

import AppRunner.Datacontainers.FileNotValidException;
import Management.Manager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class MainController {

    final FileService fs;
    final WebApplicationContext ctx;

    public MainController(FileService fs, WebApplicationContext ctx) {
        this.fs = fs;
        this.ctx = ctx;
    }

    @GetMapping ("/")
    public String index() {
        return "main";
    }

    @GetMapping ("/download")
    public String download(HttpServletResponse response) throws IOException {
        fs.makeZip(List.of("resources/tmp/index", "resources/tmp/secret"), response.getOutputStream());
        return "main";
    }

    @PostMapping ("/")
    public String fileupload(MultipartFile file, MultipartFile secretfile, Model m) {
        Manager mgr = ctx.getBean(Manager.class);
        File newfile, newsecretfile = newfile = null;


        try {
            if (!file.isEmpty()) newfile = fs.store(file, false);
            mgr.changeFile(newfile, false);
            mgr.initFile();
            mgr.loadFile();
            m.addAttribute("fileupload", "File successfully uploaded.");
            m.addAttribute("fileuploadtype", "valid-feedback");
        } catch (FileNotValidException e) {
            handleException(m, "fileupload");
        }
        try {
            if (!secretfile.isEmpty()) newsecretfile = fs.store(secretfile, true);
            mgr.changeFile(newsecretfile, true);
            mgr.initSecretfile();
            mgr.loadSecretFile();
            m.addAttribute("secretfileupload", "File successfully uploaded.");
            m.addAttribute("secretfileuploadtype", "valid-feedback");
        } catch (FileNotValidException e) {
            handleException(m, "secretfileupload");
        }


        return "main";
    }

    private void handleException(Model m, String fileupload) {
        m.addAttribute(fileupload, "File is not valid.");
        m.addAttribute(fileupload + "type", "invalid-feedback");
    }
}
