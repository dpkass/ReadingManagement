package Management;

import EntryHandling.CSVHandler;
import EntryHandling.Entry;
import EntryHandling.EntryList;
import EntryHandling.FileHandler;
import IOHandling.IOHandler;
import IOHandling.StdIOHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Manager {
    static File standardfile = new File("resources/index.csv");
    File f;

    IOHandler in;
    FileHandler fh;
    EntryList el;

    public Manager() {
        f = standardfile;
        init();
    }

    public void run() {
        for (; ; ) {
            String command = in.read();
            if (!process(command)) break;
        }
    }

    private boolean process(String s) {
        if (s == null || s.isBlank()) return true;

        String[] parts = split(s);
        switch (parts[0]) {
            case "exit" -> {return exit();}
            case "new" -> doNew(parts);
            case "read" -> doRead(parts);
            case "readto" -> doReadTo(parts);
            case "add" -> doAdd(parts);
            case "help" -> help();
            default -> errorMessage("invalid");
        }

        return true;
    }

    private boolean exit() {
        fh.write(el);
        return false;
    }

    private void help() {
        in.write("Use one of the following commands:");
        in.write("new \"{book name}\" [page read to] [link] [acronyms...]");
        in.write("read \"{book name}\" {pagecount read}");
        in.write("readto \"{book name}\" {page read to}");
        in.write("add link \"{book name}\" link");
        in.write("add acronym \"{book name}\" acronym");
        in.write("help");
        in.write("");
        in.write("Legend:");
        in.write("[...] - optional");
        in.write("{...} - one value");
        in.write("\"...\" - book name in quotes if more than one word");
    }

    private void doAdd(String[] parts) {
        if (parts.length < 2) {
            errorMessage("add");
            return;
        }

        switch (parts[1]) {
            case "link" -> doAddLink(parts);
            case "acronym", "anym" -> doAddAcronym(parts);
        }
    }

    private void doAddAcronym(String[] parts) {
        if (parts.length < 3) {
            errorMessage("add acronym");
            return;
        }

        Entry e = el.get(parts[2]);
        if (e == null) {
            errorMessage("enf");
            return;
        }

        e.addAcronyms(Arrays.stream(parts).skip(3).toArray(String[]::new));
    }

    private void doAddLink(String[] parts) {
        if (parts.length < 3 || parts.length > 4) {
            errorMessage("add link");
            return;
        }

        Entry e = el.get(parts[2]);
        if (e == null) {
            errorMessage("enf");
            return;
        }

        e.setLink(parts[3]);
    }

    private void doNew(String[] parts) {
        if (parts.length < 2) errorMessage("new");

        String[] vals = Arrays.stream(parts).skip(1).toArray(String[]::new);
        el.add(vals);
    }

    private void doReadTo(String[] parts) {
        if (parts.length != 3) {
            errorMessage("readto");
            return;
        }

        Entry e = el.get(parts[1]);
        if (e == null) {
            errorMessage("enf");
            return;
        }

        e.setReadto(Integer.parseInt(parts[2]));
    }

    private void doRead(String[] parts) {
        if (parts.length != 3) {
            errorMessage("read");
            return;
        }

        Entry e = el.get(parts[1]);
        if (e == null) {
            errorMessage("enf");
            return;
        }

        e.setReadto(e.readto() + Integer.parseInt(parts[2]));
    }

    private void init() {
        in = new StdIOHandler();
        fh = new CSVHandler(f);
        el = fh.read();
    }


    public void setFile(String f) {
        this.f = new File(f);
        init();
    }

    private void errorMessage(String error) {
        switch (error) {
            case "read", "readto" ->
                    in.write("Invalid Input. Please write \"" + error + "\", name or acronym of the book (in quotes if more than one " +
                            "word) and the page or readto number. E.g. \"" + error + " \"Solo Leveling\" 5\"");
            case "add acronym", "add link" ->
                    in.write("Invalid Input. Please write \"" + error + "\", the name or acronym of the book (in quotes if more than one word) and " +
                            "the " + error.split(" ")[1] + ". E.g. \"" + error + " \"Solo Leveling\" 5\"");
            case "new" ->
                    in.write("Invalid Input. Please write \"new\", the name or acronym of the book (in quotes if more than one word) (additional: " +
                            "the page or readto number and link  to website). E.g. \"" + error + " \"Solo Leveling\" 5\"");
            case "invalid" -> in.write("Invalid Input. Use help for more info.");
            case "enf" -> in.write("The given book was not found. If you want to add a new Entry use \"new\".");
        }
    }

    private String[] split(String command) {
        List<String> list = new ArrayList();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find())
            list.add(m.group(1).replace("\"", ""));
        return list.toArray(String[]::new);
    }
}
