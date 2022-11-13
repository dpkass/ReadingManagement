package Management.Processors;

import EntryHandling.Entry.Entry;
import Management.Helper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Adder {
    public static String add(Stream<Entry> el, Entry e, List<String> parts) {
        switch (Helper.representation(parts.get(1))) {
            case "ab" -> {
                checkAbbreviations(el, parts.get(3));
                e.addAbbreviation(parts.get(3));
            }
            default -> throw new IllegalArgumentException("1");
        }
        return "Abbreviation added.";
    }

    public static void checkAbbreviations(Stream<Entry> el, String... newAbs) {
        if (el.map(Entry::abbreviations)
              .flatMap(Collection::stream)
              .anyMatch(ab -> Arrays.asList(newAbs).contains(ab))) {
            throw new IllegalArgumentException("7");
        }
    }
}
