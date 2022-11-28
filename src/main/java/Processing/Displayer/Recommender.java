package Processing.Displayer;

import AppRunner.Datastructures.DisplayAttributesForm;
import AppRunner.Datastructures.Request;
import EntryHandling.Entry.Entry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

class Recommender {
    static void recommend(Stream<Entry> entries) {
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        String twoWeeksAgoString = twoWeeksAgo.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        String command = "List gb=rs rs=ReadingORStartedORNot-Started ws=Rolling lr<" + twoWeeksAgoString;
        Request rq = Request.parse(command);
        Lister.list(entries, DisplayAttributesForm.none(), rq.filters(), null, rq.groupby());
    }
}
