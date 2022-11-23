package EntryHandling.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class EntryList {
    final List<Entry> books = new ArrayList<>();

    public void add(List<String> values) {
        EntryBuilder eb = new EntryBuilder(values);
        Entry e = eb.toEntry();
        checkAbbreviations(e);
        books.add(e);
    }

    public void addAll(List<Object> books) {
        for (Object book : books) {
            EntryBuilder eb = new EntryBuilder((Map<String, Object>) book);
            this.books.add(eb.toEntry());
        }
    }

    public Entry get(String s) {
        return books.stream().filter(a -> a.name().equals(s) || EntryUtil.hasAbbreviation(a, s)).findAny().orElse(null);
    }

    public Stream<Entry> entries() {
        return books.stream();
    }

    public List<Entry> list() {
        return books;
    }

    private void checkAbbreviations(Entry e) {
        EntryUtil.checkAbbreviations(entries(), e.abbreviations().toArray(String[]::new));
    }
}
