package Processing.Displayer;

import AppRunner.Datacontainers.Attribute;
import AppRunner.Datacontainers.Filter;
import AppRunner.Datacontainers.Operator;
import AppRunner.Datacontainers.Request;
import Processing.Processor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

class Recommender {
    static void recommend() {
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        String twoWeeksAgoString = twoWeeksAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Request rq = new Request();
        rq.setOperator(Operator.List);
        rq.setGroupby("rs");
        Filter<?> filter = Filter.createFilter(Attribute.ReadingStatus, "=", Set.of("Reading", "Started", "Not-Started"));
        Filter<?> filter2 = Filter.createFilter(Attribute.WritingStatus, "=", Set.of("Rolling"));
        Filter<?> filter3 = Filter.createFilter(Attribute.LastRead, "<", twoWeeksAgoString);
        rq.setFilters(Set.of(filter, filter2, filter3));

        Processor.doList(rq);
    }
}
