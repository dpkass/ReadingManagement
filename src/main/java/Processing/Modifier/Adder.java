package Processing.Modifier;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;
import Management.Helper;

import java.util.List;
import java.util.stream.Stream;

class Adder {
    static String add(Stream<Entry> el, Entry e, List<String> parts) {
        switch (Helper.representation(parts.get(1))) {
            case "ab" -> {
                EntryUtil.checkAbbreviations(el, parts.get(3));
                e.addAbbreviation(parts.get(3));
            }
            default -> throw new IllegalArgumentException("1");
        }
        return "Abbreviation added.";
    }
}
