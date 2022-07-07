package EntryHandling;

import java.util.ArrayList;
import java.util.List;

public class EntryList {
    public List<Entry> list = new ArrayList<>();

    void add(String[] values) {
        list.add(new Entry(values));
    }

    void change(Entry oldEntry, Entry newEntry) {
        if (list.remove(oldEntry))
            list.add(newEntry);
    }
}
