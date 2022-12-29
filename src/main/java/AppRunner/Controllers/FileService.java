package AppRunner.Controllers;

import AppRunner.Datastructures.FileNotValidException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileService {

    File indexfile = new File("resources/tmp/index");
    File secretfile = new File("resources/tmp/secret");

    public FileService() throws IOException {
        indexfile.getParentFile().mkdirs();
        indexfile.createNewFile();
        secretfile.getParentFile().mkdirs();
        secretfile.createNewFile();
    }

    File store(MultipartFile file, boolean secret) {
        if (file == null || file.isEmpty()) return null;
        try {
            InputStream is = file.getInputStream();
            OutputStream os = new FileOutputStream(secret ? secretfile : indexfile);
            os.write(is.readAllBytes());
        } catch (IOException e) {
            throw new FileNotValidException(file.getName(), secret);
        }
        return secret ? secretfile : indexfile;
    }
}
