package Management;

import EntryHandling.Entry;
import EntryHandling.EntryList;
import IOHandling.IOHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Processor {

    static List<String> out;
    static EntryList el;

    static boolean process(String s, EntryList el, IOHandler io) {
        if (s == null || s.isBlank()) return true;

        Processor.el = el;
        out = new ArrayList<>();

        String[] parts = split(s);
        switch (parts[0]) {
            case "exit" -> {return false;}
            case "new" -> doNew(parts);
            case "read" -> doRead(parts);
            case "read-to" -> doReadTo(parts);
            case "add" -> doAdd(parts);
            case "list" -> doList(parts);
            case "list-all" -> doListAll();
            case "help" -> help();
            default -> errorMessage("invalid");
        }

        out.forEach(io::write);

        return true;
    }

    private static void doRead(String[] parts) {
        if (parts.length != 3) {
            errorMessage("invalid");
            return;
        }

        Entry e = el.get(parts[1]);
        if (e == null) {
            errorMessage("enf");
            return;
        }

        e.setReadto(e.readto() + Integer.parseInt(parts[2]));

        out.add("Read-to changed.");
    }

    private static void doListAll() {
        out.addAll(el.entries().stream().map(Entry::toString).collect(Collectors.toSet()));
    }

    private static void doList(String[] parts) {
        if (parts.length == 1) {
            doListNames();
            return;
        }

        switch (parts[1]) {
            case "name", "names" -> doListNames();
            case "link", "links" -> doListLink();
            case "read-to", "readto" -> doListReadto();
            case "acronym", "acronyms", "anym", "anyms" -> doListAcronyms();
            default -> errorMessage("invalid");
        }
    }

    private static void doListNames() {
        out.addAll(el.entries().stream().map(Entry::name).toList());
    }

    private static void doListLink() {
        out.addAll(el.entries().stream().map(e -> e.name() + " --> " + e.link()).toList());
    }

    private static void doListReadto() {
        out.addAll(el.entries().stream().map(e -> e.name() + " --> " + e.link()).toList());
    }

    private static void doListAcronyms() {
        out.addAll(el.entries().stream().map(e -> e.name() + " --> " + e.acronyms().toString()).toList());
    }

    private static void help() {
        out.add("Use one of the following commands:");
        out.add("new \"{book name}\" [page read to] [link] [acronyms...]");
        out.add("read \"{book name}\" {pagecount read}");
        out.add("read-to \"{book name}\" {page read to}");
        out.add("list [option (standard:name)]");
        out.add("list-all");
        out.add("add [option] \"{book name}\" {value}");
        out.add("help");
        out.add("");
        out.add("Legend:");
        out.add("[...] - optional");
        out.add("{...} - parameter");
        out.add("\"...\" - book name in quotes if more than one word");
    }

    private static void doAdd(String[] parts) {
        if (parts.length < 2) {
            errorMessage("add");
            return;
        }

        switch (parts[1]) {
            case "link" -> doAddLink(parts);
            case "acronym", "anym" -> doAddAcronym(parts);
        }
    }

    private static void doAddAcronym(String[] parts) {
        if (parts.length < 3) {
            errorMessage("invalid");
            return;
        }

        Entry e = el.get(parts[2]);
        if (e == null) {
            errorMessage("enf");
            return;
        }

        e.addAcronyms(Arrays.stream(parts).skip(3).toArray(String[]::new));

        out.add("Acronym added.");
    }

    private static void doAddLink(String[] parts) {
        if (parts.length < 3 || parts.length > 4) {
            errorMessage("invalid");
            return;
        }

        Entry e = el.get(parts[2]);
        if (e == null) {
            errorMessage("enf");
            return;
        }

        e.setLink(parts[3]);

        out.add("Link added.");
    }

    private static void doNew(String[] parts) {
        if (parts.length < 2) errorMessage("invalid");

        Entry e = el.get(parts[1]);
        if (e != null) {
            errorMessage("book already there");
            return;
        }

        String[] vals = Arrays.stream(parts).skip(1).toArray(String[]::new);
        el.add(vals);

        out.add("Entry added.");
    }

    private static void doReadTo(String[] parts) {
        if (parts.length != 3) {
            errorMessage("invalid");
            return;
        }

        Entry e = el.get(parts[1]);
        if (e == null) {
            errorMessage("enf");
            return;
        }

        e.setReadto(Integer.parseInt(parts[2]));

        out.add("Read-to changed.");
    }

    private static void errorMessage(String error) {
        switch (error) {
            case "invalid" -> out.add("Invalid Input. Use help for more info.");
            case "book already there" -> out.add("The given book is already in the list.");
            case "enf" -> out.add("The given book was not found. If you want to add a new Entry use \"new\".");
        }
    }

    private static String[] split(String command) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find())
            list.add(m.group(1).replace("\"", ""));
        return list.toArray(String[]::new);
    }
}
