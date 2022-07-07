package EntryHandling;

import java.util.ArrayList;
import java.util.List;

public class EntryList {
    public List<Entry> list = new ArrayList<>();

    public void add(String[] values) {
        list.add(new Entry(values));
    }

    public Entry get(String s) {
        return list.stream().filter(a -> a.name().equals(s) || a.hasAcronym(s)).findAny().orElse(null);
    }
}
