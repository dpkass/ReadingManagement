package Management;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

class Processor {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM HH:mm");
    static List<String> out;
    static EntryList el;

    static void doAdd(String[] parts) {
        if (parts.length < 4) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = getEntry(parts[2]);
        if (e == null) return;

        switch (Helper.representation(parts[1])) {
            case "ab" -> e.addAbbreviation(parts[3]);
            default -> out.add(Helper.errorMessage("invalid"));
        }

        out.add("Abbreviation added.");
    }

    static void doChange(String[] parts) {
        if (parts.length < 4) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = getEntry(parts[2]);
        if (e == null) return;

        switch (Helper.representation(parts[1])) {
            case "n" -> e.setName(parts[3]);
            case "lk" -> e.setLink(parts[3]);
            case "ws" -> e.setWritingStatus(parts[3]);
            case "rs" -> e.setReadingStatus(parts[3]);
            case "ab" -> {
                e.removeAbbreviation(parts[2]);
                e.addAbbreviation(parts[3]);
            }
            default -> out.add(Helper.errorMessage("invalid"));
        }

        out.add("Entry changed.");
    }

    static void doList(String[] parts) {
        if (parts.length == 1) {
            out.addAll(el.entries().stream().map(Entry::name).toList());
            return;
        }

        Function<Entry, String> f = e -> switch (Helper.representation(parts[1])) {
            case "n" -> e.name();
            case "lk" -> e.link();
            case "lr" -> e.lastread().format(dtf);
            case "ws" -> Objects.toString(e.writingStatus());
            case "rs" -> Objects.toString(e.readingStatus());
            case "r", "rt" -> e.readto();
            case "ab" -> e.abbreviations().toString();
            default -> {throw new RuntimeException();}
        };
        out.addAll(el.entries().stream().map(e -> e.name() + " --> " + f.apply(e)).toList());
    }

    static void doListAll() {
        out.addAll(el.entries().stream().map(Entry::toString).collect(Collectors.toSet()));
    }

    public static void doShow(String[] parts) {
        Entry e = getEntry(parts[1]);
        if (e == null) return;

        if (parts.length == 2) out.add(e.toString());

        for (int i = 2; i < parts.length; i++) {
            out.add(switch (Helper.representation(parts[i])) {
                case "r", "rt" -> "read-to: %s".formatted(e.readto());
                case "lk" -> "link: %s".formatted(e.link());
                case "lr" -> "lastread: %s".formatted(e.lastread().format(dtf));
                case "ws" -> "writing-status: %s".formatted(e.writingStatus());
                case "rs" -> "reading-status: %s".formatted(e.readingStatus());
                case "ab" -> "abbreviations: %s".formatted(e.abbreviations().toString());
                default -> throw new IllegalStateException("Unexpected value: " + parts[i]);
            });
        }
    }

    static void doNew(String[] parts) {
        if (parts.length < 2) out.add(Helper.errorMessage("invalid"));

        Entry e = el.get(parts[1]);
        if (e != null) {
            out.add(Helper.errorMessage("duplicate"));
            return;
        }

        String[] vals = Arrays.stream(parts).skip(1).toArray(String[]::new);
        el.add(vals);

        out.add("Entry added.");
    }

    public static void doOpen(String[] parts) {
        if (parts.length != 2) out.add(Helper.errorMessage("invalid"));

        Entry e = getEntry(parts[1]);
        if (e == null) return;

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                URI link = new URI(e.link());
                Desktop.getDesktop().browse(link);
            } catch (URISyntaxException | IOException ex) {
                out.add(Helper.errorMessage("link wrong"));
                out.add(e.name() + " -> " + e.link());
            }
        } else {
            out.add(Helper.errorMessage("no os support"));
            out.add(e.name() + " -> " + e.link());
        }
    }

    static void doRead(String[] parts) {
        if (parts.length != 3) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = getEntry(parts[1]);
        if (e == null) return;

        if (e.addRead(parts[2])) {
            out.add("Read-to changed.");
            e.setLastread(LocalDateTime.now());
        } else out.add(Helper.errorMessage("read not number"));
    }

    static void doReadTo(String[] parts) {
        if (parts.length != 3) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = getEntry(parts[1]);
        if (e == null) return;

        e.setReadto(parts[2]);
        e.setLastread(LocalDateTime.now());

        out.add("Read-to changed.");
    }

    private static Entry getEntry(String part) {
        Entry e = el.get(part);
        if (e == null) {
            out.add(Helper.errorMessage("enf"));
            return null;
        }
        return e;
    }
}
