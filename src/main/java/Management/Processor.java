package Management;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import Management.Processors.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

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

        Adder.add(e, parts);

        out.add("Abbreviation added.");
    }

    static void doChange(String[] parts) {
        if (parts.length < 4) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = getEntry(parts[2]);
        if (e == null) return;

        Changer.change(e, parts);

        out.add("Entry changed.");
    }

    static void doList(String[] parts) {
        out.addAll(Lister.list(parts, el.entries().stream()));
    }

    static void doListAll() {
        out.addAll(Lister.listAll(el.entries().stream()));
    }

    public static void doShow(String[] parts) {
        Entry e = getEntry(parts[1]);
        if (e == null) return;
        out.addAll(Shower.show(e, parts));
    }

    static void doNew(String[] parts) {
        if (parts.length < 2) out.add(Helper.errorMessage("invalid"));

        Entry e = el.get(parts[1]);
        if (e != null) {
            out.add(Helper.errorMessage("duplicate"));
            return;
        }

        out.add(Newer.make(el, parts));
    }

    public static void doOpen(String[] parts) {
        if (parts.length != 2) out.add(Helper.errorMessage("invalid"));

        Entry e = getEntry(parts[1]);
        if (e == null) return;

        Opener.open(e);
    }

    static void doRead(String[] parts) {
        if (parts.length != 3) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = getEntry(parts[1]);
        if (e == null) return;

        out.add(Reader.read(e, parts[2]));
    }

    static void doReadTo(String[] parts) {
        if (parts.length != 3) {
            out.add(Helper.errorMessage("invalid"));
            return;
        }

        Entry e = getEntry(parts[1]);
        if (e == null) return;

        out.add(Reader.readto(e, parts[2]));
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
