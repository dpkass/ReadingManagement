package EntryHandling.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntryList {
    List<Entry> books = new ArrayList<>();

    public void add(String[] values) {
        EntryBuilder eb = new EntryBuilder(values);
        books.add(eb.toEntry());
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

    public List<Entry> entries() {
        return books;
    }

    public void encode() {
        books.forEach(EntryUtil::encode);
    }

    public void decode() {
        books.forEach(EntryUtil::decode);
    }

    public List<Entry> list() {
        return books;
    }
}
