package AppRunner.Controllers;

import Management.Manager;
import org.springframework.stereotype.Controller;
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
    public String fileupload(MultipartFile file, MultipartFile secretfile) {
        Manager mgr = ctx.getBean(Manager.class);

        File newfile, newsecretfile = newfile = null;
        if (!file.isEmpty()) newfile = fs.store(file, false);
        if (!secretfile.isEmpty()) newsecretfile = fs.store(secretfile, true);
        mgr.changeFiles(newfile, newsecretfile);
        mgr.init();
        mgr.load();
        return "main";
    }
}
