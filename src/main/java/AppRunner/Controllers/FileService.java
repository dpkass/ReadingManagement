package AppRunner.Controllers;

import AppRunner.Datacontainers.FileNotValidException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    void makeZip(List<String> files, ServletOutputStream responseOut) {
        try (ZipOutputStream zippedOut = new ZipOutputStream(responseOut)) {
            for (String file : files) {
                InputStream resourceInputStream = getZipEntryAsInputStream(zippedOut, file);
                // And the content of the resource:
                StreamUtils.copy(resourceInputStream, zippedOut);
                zippedOut.closeEntry();
            }
            zippedOut.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private InputStream getZipEntryAsInputStream(ZipOutputStream zippedOut, String file) throws IOException {
        FileSystemResource resource = new FileSystemResource(file);

        ZipEntry e = new ZipEntry(resource.getFilename());
        // Configure the zip entry, the properties of the file
        e.setSize(resource.contentLength());
        e.setTime(System.currentTimeMillis());
        zippedOut.putNextEntry(e);
        InputStream resourceInputStream = resource.getInputStream();
        return resourceInputStream;
    }
}
