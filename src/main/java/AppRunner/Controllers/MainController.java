package AppRunner.Controllers;

import Management.Manager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
        return "/index";
    }

    @PostMapping ("/")
    public String fileupload(MultipartFile file, MultipartFile secretfile) {
        Manager mgr = ctx.getBean(Manager.class);

        File newfile, newsecretfile = newfile = null;
        if (!file.isEmpty()) newfile = fs.store(file, false);
        if (!secretfile.isEmpty()) newsecretfile = fs.store(secretfile, true);
        mgr.changeFiles(newfile, newsecretfile);
        mgr.init();
        mgr.load();
        return "/index";
    }
}
