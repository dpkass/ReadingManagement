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

    ProcessStarter ps;

    public Manager() throws IOException {
        resetFiles();
        file = standardfile;
        secretfile = standardsecretfile;
        init();
    }

    private void resetFiles() throws IOException {
        createFile(standardfile);
        createFile(standardsecretfile);

    }

    private void createFile(File file) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    public String files() {
        return file.toString() + '\n' + secretfile;
    }

    public void init() {
        fh = new JSONHandler(file, false);
        secretfh = new JSONHandler(secretfile, true);
    }

    public void load() {
        el = fh.read();
        secretel = secretfh.read();
        ps = new ProcessStarter(el, secretel, rr);
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

    public void changeFiles(File file, File secretfile) {
        this.file = file == null ? this.file : file;
        this.secretfile = secretfile == null ? this.secretfile : secretfile;
    }

    public Stream<Entry> entries() {
        return el.entries();
    }
}
