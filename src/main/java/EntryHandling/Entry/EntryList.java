package EntryHandling.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntryList {
    List<Entry> list = new ArrayList<>();

    public void add(String[] values) {
        EntryBuilder eb = new EntryBuilder(values);
        list.add(eb.toEntry());
    }

    public Entry get(String s) {
        return list.stream().filter(a -> a.name().equals(s) || EntryUtil.hasAbbreviation(a, s)).findAny().orElse(null);
    }

    public List<Entry> entries() {
        return list;
    }

    public void encode() {
        list.forEach(EntryUtil::encode);
    }

    public void decode() {
        list.forEach(EntryUtil::decode);
    }

    public List<Entry> list() {
        return list;
    }
}
