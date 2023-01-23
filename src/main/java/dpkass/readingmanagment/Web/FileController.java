package dpkass.readingmanagment.Web;

import dpkass.readingmanagment.Domain.Exceptions.FileNotValidException;
import dpkass.readingmanagment.Core.Management.Manager;
import dpkass.readingmanagment.WebService.FileService;
import dpkass.readingmanagment.WebService.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
public class FileController {

    final FileService fs;
    final WebApplicationContext ctx;

    public FileController(FileService fs, WebApplicationContext ctx) {
        this.fs = fs;
        this.ctx = ctx;
    }

    @GetMapping ("/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping ("/upload")
    public String fileupload(MultipartFile file, MultipartFile secretfile, Model m) {
        Manager mgr = ctx.getBean(Manager.class);

        handleFile(file, m, mgr);
        handleSecretfile(secretfile, m, mgr);

        return "upload";
    }

    @GetMapping ("/ReadingManager-Files")
    public String download(HttpServletResponse response) throws IOException {
        RequestService rs = ctx.getBean(RequestService.class);
        fs.makeZip(rs.files(), response.getOutputStream());
        return "index";
    }

    private void handleFile(MultipartFile file, Model m, Manager mgr) {
        if (file.isEmpty()) return;

        File oldfile = mgr.file();
        try {
            File newfile = fs.store(file, false);
            mgr.setFile(newfile);
            mgr.loadFile();
            m.addAttribute("fileupload", "File successfully uploaded.");
            m.addAttribute("fileuploadtype", "valid-feedback");
        } catch (FileNotValidException e) {
            mgr.setFile(oldfile);
            handleException(m, "fileupload");
        }
    }

    private void handleSecretfile(MultipartFile secretfile, Model m, Manager mgr) {
        if (secretfile.isEmpty()) return;

        File oldsecretfile = mgr.secretfile();
        try {
            File newsecretfile = fs.store(secretfile, true);
            mgr.setSecretfile(newsecretfile);
            mgr.loadSecretFile();
            m.addAttribute("secretfileupload", "File successfully uploaded.");
            m.addAttribute("secretfileuploadtype", "valid-feedback");
        } catch (FileNotValidException e) {
            mgr.setSecretfile(oldsecretfile);
            handleException(m, "secretfileupload");
        }
    }

    private void handleException(Model m, String fileupload) {
        m.addAttribute(fileupload, "File is not valid.");
        m.addAttribute(fileupload + "type", "invalid-feedback");
    }
}
