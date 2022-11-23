package Processing.Displayer;

import EntryHandling.Entry.Entry;

import java.util.List;
import java.util.stream.Stream;

public class Displayer {
    public static void list(List<String> parts, Stream<Entry> entries) {
        Lister.list(parts.stream().skip(1), entries);
    }

    public static void listAll(Stream<Entry> entries) {
        Stream<String> allattributes = DisplayUtil.allAttributes();
        Lister.list(allattributes, entries);
    }

    public static void recommend(Stream<Entry> entries) {
        Recommender.recommend(entries);
    }

    public static void show(Entry e, List<String> parts) {
        Shower.show(e, parts);
    }
}
