package Processing.Modifier;

import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;

import java.util.stream.Stream;

class Adder {
    static String add(Entry e, String newVal, Stream<Entry> el) {
        EntryUtil.checkAbbreviations(el, newVal);
        e.addAbbreviation(newVal);
        return "Abbreviation added.";
    }
}