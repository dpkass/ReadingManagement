package Management.Processors;

import EntryHandling.Entry.Entry;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Recommender {

    public static Collection<String> recommend(Stream<Entry> entries) {
        String[] command = "l gb=rs rs=ReadingORStartedORNot-Started ws=Rolling".split(" ");
        return Lister.list(List.of(command), entries);
    }
}
