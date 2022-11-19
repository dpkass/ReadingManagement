package Management.Processors;

import EntryHandling.Entry.Entry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Recommender {

    public static Collection<String> recommend(Stream<Entry> entries) {
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        String twoWeeksAgoString = twoWeeksAgo.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String[] command = "l gb=rs rs=ReadingORStartedORNot-Started ws=Rolling lr<%s".formatted(twoWeeksAgoString)
                                                                                      .split(" ");
        return Lister.list(List.of(command), entries);
    }
}
