package dpkass.readingmanagment.Core.Processing;

import dpkass.readingmanagment.Domain.Aggregates.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TableDataSupplier {
    private final List<Function<Book, String>> suppliers = new ArrayList<>();

    public TableDataSupplier() {
        this.suppliers.add(Book::name);
    }

    public void add(Function<Book, String> func) {
        suppliers.add(func);
    }

    public List<Function<Book, String>> suppliers() {
        return suppliers;
    }
}
