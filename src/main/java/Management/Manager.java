package Management;

import AppRunner.Datacontainers.Request;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.FileHandler;
import EntryHandling.JSONHandler;
import Processing.RequestResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Component
@Scope ("singleton")
public class Manager {
    public static final File standardfile = new File("resources/standard_index");
    public static final File standardsecretfile = new File("resources/standard_secret");

    File file;
    File secretfile;

    RequestResult rr = new RequestResult();
    FileHandler fh;
    FileHandler secretfh;
    EntryList el;
    EntryList secretel;

    ProcessStarter ps = new ProcessStarter(rr);

    public Manager() throws IOException {
        resetFiles();
        setFile(standardfile);
        setSecretfile(standardsecretfile);
    }

    public void load() {
        loadFile();
        loadSecretFile();
    }

    private void resetFiles() throws IOException {
        createFile(standardfile);
        createFile(standardsecretfile);
    }

    private void createFile(File file) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    public void initFile() {
        fh = new JSONHandler(file, false);
    }

    public void loadFile() {
        el = fh.read();
        ps.setEl(el);
    }

    public void initSecretfile() {
        secretfh = new JSONHandler(secretfile, true);
    }

    public void loadSecretFile() {
        secretel = secretfh.read();
        ps.setSecretel(secretel);
    }

    private void save() {
        fh.write(el);
        secretfh.write(secretel);
    }

    public RequestResult process(Request r) {
        rr.clear();
        ps.process(r);
        save();
        return rr.copy();
    }

    public void setFile(File file) {
        this.file = file == null ? this.file : file;
        initFile();
    }

    public void setSecretfile(File file) {
        this.secretfile = file == null ? this.secretfile : file;
        initSecretfile();
    }

    public Stream<Entry> entries() {
        return el.entries();
    }

    public File file() {
        return file;
    }

    public File secretfile() {
        return secretfile;
    }
}
