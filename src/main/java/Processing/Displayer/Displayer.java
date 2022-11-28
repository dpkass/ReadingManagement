package Processing.Displayer;

import AppRunner.Datastructures.Attribute;
import AppRunner.Datastructures.DisplayAttributesForm;
import AppRunner.Datastructures.Filter;
import EntryHandling.Entry.Entry;

import java.util.List;
import java.util.stream.Stream;

public class Displayer {
    public static void list(Stream<Entry> entries, DisplayAttributesForm daf, List<Filter<?>> filters, Attribute sortby, Attribute groupby, boolean sortdescending, boolean groupdescending) {
        Lister.list(entries, daf, filters, sortby, groupby, sortdescending, groupdescending);
    }

    public static void listAll(Stream<Entry> entries) {
        Lister.list(entries, DisplayAttributesForm.all(), null, null, null, false, false);
    }

    public static void recommend(Stream<Entry> entries) {
        Recommender.recommend(entries);
    }

    public static void show(Entry e, DisplayAttributesForm daf) {
        Shower.show(e, daf);
    }
}
