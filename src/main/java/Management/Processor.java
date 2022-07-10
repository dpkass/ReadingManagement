package Management;

import EntryHandling.Entry;
import EntryHandling.EntryList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Processor {

    static List<String> output;
    static EntryList el;

    static {
        output = new ArrayList<>();
    }

    static List<String> process(String s) {
        if (s == null || s.isBlank()) return output;

        String[] parts = split(s);
        switch (parts[0]) {
            case "exit" -> {return null;}
            case "new" -> doNew(parts);
            case "read" -> doRead(parts);
            case "read-to" -> doReadTo(parts);
            case "add" -> doAdd(parts);
            case "list" -> doList(parts);
            case "list-all" -> doListAll();
            case "help" -> help();
            default -> errorMessage("invalid");
        }

        return output;
    }

    static void doRead(String[] parts) {
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

        output.add("Read-to changed.");
    }

    static void doListAll() {
        output.addAll(el.entries().stream().map(Entry::toString).collect(Collectors.toSet()));
    }

    static void doList(String[] parts) {
        if (parts.length == 1) doListNames();
    }

    private static void doListNames() {
        output.addAll(el.entries().stream().map(Entry::name).collect(Collectors.toSet()));
    }

    static void help() {
        output.add("Use one of the following commands:");
        output.add("new \"{book name}\" [page read to] [link] [acronyms...]");
        output.add("read \"{book name}\" {pagecount read}");
        output.add("read-to \"{book name}\" {page read to}");
        output.add("list");
        output.add("list-all");
        output.add("add link \"{book name}\" link");
        output.add("add acronym \"{book name}\" acronym");
        output.add("help");
        output.add("");
        output.add("Legend:");
        output.add("[...] - optional");
        output.add("{...} - parameter");
        output.add("\"...\" - book name in quotes if more than one word");
    }

    static void doAdd(String[] parts) {
        if (parts.length < 2) {
            errorMessage("add");
            return;
        }

        switch (parts[1]) {
            case "link" -> doAddLink(parts);
            case "acronym", "anym" -> doAddAcronym(parts);
        }
    }

    static void doAddAcronym(String[] parts) {
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

        output.add("Acronym added.");
    }

    static void doAddLink(String[] parts) {
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

        output.add("Link added.");
    }

    static void doNew(String[] parts) {
        if (parts.length < 2) errorMessage("new");

        Entry e = el.get(parts[1]);
        if (e != null) {
            errorMessage("book already there");
            return;
        }

        String[] vals = Arrays.stream(parts).skip(1).toArray(String[]::new);
        el.add(vals);

        output.add("Entry added.");
    }

    static void doReadTo(String[] parts) {
        if (parts.length != 3) {
            errorMessage("read-to");
            return;
        }

        Entry e = el.get(parts[1]);
        if (e == null) {
            errorMessage("enf");
            return;
        }

        e.setReadto(Integer.parseInt(parts[2]));

        output.add("Read-to changed.");
    }

    static void errorMessage(String error) {
        switch (error) {
            case "read", "read-to" ->
                    output.add("Invalid Input. Please write \"" + error + "\", name or acronym of the book (in quotes if more than one " +
                            "word) and the page or readto number. E.g. \"" + error + " \"Solo Leveling\" 5\"");
            case "add acronym", "add link" ->
                    output.add("Invalid Input. Please write \"" + error + "\", the name or acronym of the book (in quotes if more than one " +
                            "word)" +
                            " " +
                            "and " +
                            "the " + error.split(" ")[1] + ". E.g. \"" + error + " \"Solo Leveling\" 5\"");
            case "new" ->
                    output.add("Invalid Input. Please write \"new\", the name or acronym of the book (in quotes if more than one word) " +
                            "(additional: " +
                            "the page or readto number and link  to website). E.g. \"" + error + " \"Solo Leveling\" 5\"");
            case "book already there" -> output.add("The given book is already in the list.");
            case "invalid" -> output.add("Invalid Input. Use help for more info.");
            case "enf" -> output.add("The given book was not found. If you want to add a new Entry use \"new\".");
        }
    }

    static String[] split(String command) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find())
            list.add(m.group(1).replace("\"", ""));
        return list.toArray(String[]::new);
    }
}
