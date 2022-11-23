package Processing.Modifier;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryList;
import Processing.RequestResult;

import java.util.List;
import java.util.stream.Stream;

import static Processing.Processor.rr;

public class Modifier {

    public static void add(Stream<Entry> entries, Entry e, List<String> parts) {
        String res = Adder.add(entries, e, parts);
        finish(res);
    }

    public static void change(Entry e, List<String> parts) {
        String res = Changer.change(e, parts);
        finish(res);
    }


    public static void make(EntryList el, List<String> parts) {
        String res = Newer.make(el, parts);
        finish(res);
    }

    public static void read(Entry e, String s) {
        String res = Reader.read(e, s);
        finish(res);
    }

    public static void readto(Entry e, String s) {
        String res = Reader.readto(e, s);
        finish(res);
    }

    private static void finish(String res) {
        rr.setString(res);
        rr.setType(RequestResult.RequestResultType.SUCCESS);
    }
}
