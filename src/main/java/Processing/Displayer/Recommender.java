package Processing.Displayer;

import EntryHandling.Entry.Entry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

class Recommender {
    static void recommend(Stream<Entry> entries) {
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        String twoWeeksAgoString = twoWeeksAgo.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        
        Stream<String> command = Stream.of("gb=rs", "rs=ReadingORStartedORNot-Started", "ws=Rolling", "lr<" + twoWeeksAgoString);
        Lister.list(command, entries);
    }
}
