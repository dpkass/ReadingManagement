package Management;

import EntryHandling.Entry;
import EntryHandling.EntryList;
import IOHandling.IOHandler;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Processor {

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
        switch (Helper.representation(parts[0])) {
            case "e" -> {return false;}
            case "nw" -> doNew(parts);
            case "r" -> doRead(parts);
            case "rt" -> doReadTo(parts);
            case "a" -> doAdd(parts);
            case "c" -> doChange(parts);
            case "l" -> doList(parts);
            case "la" -> doListAll();
            case "s" -> doSecret(parts);
            case "h" -> io.write(Helper.help(parts));
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

        Entry e = getEntry(parts, 1);
        if (e == null) return;

        double rtVal;
        NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

        // try us standard
        try {
            rtVal = NumberFormat.getInstance(Locale.US).parse(e.readto()).doubleValue();
            e.setReadto(String.valueOf(rtVal + Integer.parseInt(parts[2])));

            out.add("Read-to changed.");
            return;
        } catch (ParseException ignored) {}

        // try europe standard
        try {
            rtVal = NumberFormat.getInstance(Locale.GERMANY).parse(e.readto()).doubleValue();
            e.setReadto(String.valueOf(rtVal + Integer.parseInt(parts[2])));

            out.add("Read-to changed.");
            return;
        } catch (ParseException ignored) {}

        out.add(Helper.errorMessage("read-to not number"));
    }

    private static void doListAll() {
        out.addAll(el.entries().stream().map(Entry::toString).collect(Collectors.toSet()));
    }

    private static void doList(String[] parts) {
        if (parts.length == 1) {
            doListNames();
            return;
        }

        switch (Helper.representation(parts[1])) {
            case "n" -> doListNames();
            case "l" -> doListLink();
            case "r", "rt" -> doListReadto();
            case "ac" -> doListAcronyms();
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

        Entry e = getEntry(parts, 2);
        if (e == null) return;

        switch (Helper.representation(parts[1])) {
            case "n" -> e.setName(parts[3]);
            case "l" -> e.setLink(parts[3]);
            case "ac" -> {
                e.removeAcronym(parts[2]);
                e.addAcronym(parts[3]);
            }
            default -> out.add(Helper.errorMessage("invalid"));
        }
    }

    private static void doAdd(String[] parts) {
        if (parts.length < 4) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = getEntry(parts, 2);
        if (e == null) return;

        switch (Helper.representation(parts[1])) {
            case "a" -> e.addAcronym(parts[3]);
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

        Entry e = getEntry(parts, 1);
        if (e == null) return;

        e.setReadto(parts[2]);

        out.add("Read-to changed.");
    }

    private static Entry getEntry(String[] parts, int x) {
        Entry e = el.get(parts[x]);
        if (e == null) {
            out.add(Helper.errorMessage("enf"));
            return null;
        }
        return e;
    }

    private static String[] split(String command) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find())
            list.add(m.group(1).replace("\"", ""));
        return list.toArray(String[]::new);
    }
}
