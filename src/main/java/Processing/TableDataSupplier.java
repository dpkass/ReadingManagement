package Processing;

import EntryHandling.Entry.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TableDataSupplier {
    private final List<Function<Entry, String>> suppliers = new ArrayList<>();

    public TableDataSupplier() {
        this.suppliers.add(Entry::name);
    }

    public void add(Function<Entry, String> func) {
        suppliers.add(func);
    }

    public List<Function<Entry, String>> suppliers() {
        return suppliers;
    }
}
