package Management;

import AppRunner.Datastructures.Request;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import EntryHandling.FileHandler;
import EntryHandling.JSONHandler;
import Processing.RequestResult;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.stream.Stream;

@Component
public class Manager {
    public static final File standardfile = new File("resources/index.json");
    public static final File standardsecretfile = new File("resources/secret.json");

    File file;
    File secretfile;

    RequestResult rr = new RequestResult();
    FileHandler fh;
    FileHandler secretfh;
    EntryList el;
    EntryList secretel;

    ProcessStarter ps;

    public Manager() {
        file = standardfile;
        secretfile = standardsecretfile;
        init();
    }

    public void init() {
        fh = new JSONHandler(file, false);
        secretfh = new JSONHandler(secretfile, true);
    }

    public void start() {
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

    public Stream<Entry> entries() {
        return el.entries();
    }
}
