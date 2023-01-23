package dpkass.readingmanagment.Core.Management;

import dpkass.readingmanagment.Domain.Aggregates.Book;
import dpkass.readingmanagment.Domain.Aggregates.Request;
import dpkass.readingmanagment.Domain.Aggregates.RequestResult;
import dpkass.readingmanagment.Persistence.EntryRepository;
import dpkass.readingmanagment.Core.FileHandling.FileHandler;
import dpkass.readingmanagment.Core.FileHandling.JSONHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

@Component
public class Manager {
    public static final File standardfile = new File("resources/standard_index");
    public static final File standardsecretfile = new File("resources/standard_secret");

    File file;
    File secretfile;

    RequestResult rr = new RequestResult();
    FileHandler fh = new JSONHandler(false);
    FileHandler secretfh = new JSONHandler(true);
    EntryRepository repo;
    EntryRepository secretrepo;

    ProcessStarter ps;

    public Manager() throws IOException {
        ps = new ProcessStarter(rr);
        resetFiles();
        setFile(standardfile);
        setSecretfile(standardsecretfile);
    }

    public void load() {
        loadFile();
        loadSecretFile();
    }

    private void resetFiles() throws IOException {
        createEmptyFile(standardfile);
        createEmptyFile(standardsecretfile);
    }

    private void createEmptyFile(File file) throws IOException {
        file.getParentFile().mkdirs();
        new FileWriter(file).close();
    }

    public void initFile() {
        fh.setFile(file);
    }

    public void loadFile() {
        repo = fh.read();
        ps.setRepo(repo);
    }

    public void initSecretfile() {
        secretfh.setFile(secretfile);
    }

    public void loadSecretFile() {
        secretrepo = secretfh.read();
        ps.setSecretrepo(secretrepo);
    }

    private void save() {
        fh.write(repo);
        secretfh.write(secretrepo);
    }

    public RequestResult process(Request r) {
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

    public Stream<Book> entries() {
        return repo.entries();
    }

    public Stream<Book> secretentries() {
        return secretrepo.entries();
    }

    public File file() {
        return file;
    }

    public File secretfile() {
        return secretfile;
    }
}
