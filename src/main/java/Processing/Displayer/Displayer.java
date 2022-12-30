package Processing.Displayer;

import AppRunner.Datacontainers.Attribute;
import AppRunner.Datacontainers.DisplayAttributesForm;
import AppRunner.Datacontainers.Filter;
import EntryHandling.Entry.Entry;

import java.util.Set;
import java.util.stream.Stream;

public class Displayer {
    public static void list(Stream<Entry> entries, DisplayAttributesForm daf, Set<Filter<?>> filters, Attribute sortby, Attribute groupby, boolean sortdescending, boolean groupdescending) {
        Lister.list(entries, daf, filters, sortby, groupby, sortdescending, groupdescending);
    }

    public static void recommend() {
        Recommender.recommend();
    }
}
