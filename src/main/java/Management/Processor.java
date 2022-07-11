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
    static IOHandler io;
    static EntryList el;

    static boolean process(String s, EntryList el, EntryList secretel, IOHandler io) {
        if (s == null || s.isBlank()) return true;

        String[] parts = split(s);

        switch (parts[0]) {
            case "secret" -> Processor.el = secretel;
            default -> Processor.el = el;
        }

        Processor.io = io;
        out = new ArrayList<>();

        boolean b = process(parts);

        out.forEach(io::write);

        return b;
    }

    static boolean process(String[] parts) {
        switch (parts[0]) {
            case "exit" -> {return false;}
            case "new" -> doNew(parts);
            case "read" -> doRead(parts);
            case "read-to" -> doReadTo(parts);
            case "change" -> doChange(parts);
            case "list" -> doList(parts);
            case "list-all" -> doListAll();
            case "secret" -> doSecret(parts);
            case "help" -> io.write(Helper.help(parts));
            default -> out.add(Helper.errorMessage("invalid"));
        }
        return true;
    }

    private static void doSecret(String[] parts) {
        parts = Arrays.stream(parts).skip(1).toArray(String[]::new);
        process(parts);
    }

    private static void doRead(String[] parts) {
        if (parts.length != 3) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = el.get(parts[1]);
        if (e == null) {
            out.add(Helper.errorMessage("enf"));
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

        switch (representation(parts[1])) {
            case 'n' -> doListNames();
            case 'l' -> doListLink();
            case 'r' -> doListReadto();
            case 'a' -> doListAcronyms();
            default -> out.add(Helper.errorMessage("invalid"));
        }
    }

    private static void doListNames() {
        out.addAll(el.entries().stream().map(Entry::name).toList());
    }

    private static void doListLink() {
        out.addAll(el.entries().stream().map(e -> e.name() + " --> " + e.link()).toList());
    }

    private static void doListReadto() {
        out.addAll(el.entries().stream().map(e -> e.name() + " --> " + e.readto()).toList());
    }

    private static void doListAcronyms() {
        out.addAll(el.entries().stream().map(e -> e.name() + " --> " + e.acronyms().toString()).toList());
    }

    private static void doChange(String[] parts) {
        if (parts.length < 4) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = el.get(parts[2]);
        if (e == null) {
            out.add(Helper.errorMessage("enf"));
            return;
        }

        switch (representation(parts[1])) {
            case 'n' -> e.setName(parts[3]);
            case 'l' -> e.setLink(parts[3]);
            case 'a' -> {
                e.removeAcronym(parts[2]);
                e.addAcronym(parts[3]);
            }
            default -> out.add(Helper.errorMessage("invalid"));
        }
    }

    private static void doNew(String[] parts) {
        if (parts.length < 2) out.add(Helper.errorMessage("invalid"));

        Entry e = el.get(parts[1]);
        if (e != null) {
            out.add(Helper.errorMessage("book already there"));
            return;
        }

        String[] vals = Arrays.stream(parts).skip(1).toArray(String[]::new);
        el.add(vals);

        out.add("Entry added.");
    }

    private static void doReadTo(String[] parts) {
        if (parts.length != 3) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = el.get(parts[1]);
        if (e == null) {
            out.add(Helper.errorMessage("enf"));
            return;
        }

        e.setReadto(Integer.parseInt(parts[2]));

        out.add("Read-to changed.");
    }

    private static String[] split(String command) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find())
            list.add(m.group(1).replace("\"", ""));
        return list.toArray(String[]::new);
    }

    private static char representation(String part) {
        return switch (part) {
            case "names", "name", "n" -> 'n';
            case "links", "link", "l" -> 'l';
            case "read-to", "readto", "r" -> 'r';
            case "acronyms", "acronym", "anyms", "anym", "a" -> 'a';
            default -> ' ';
        };
    }
}
