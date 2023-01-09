package EntryHandling.Entry;

import AppRunner.Data.Types.Booktype;
import AppRunner.Data.Types.Genre;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class EntryList {
    final List<Entry> books = new ArrayList<>();

    public void add(List<String> values) {
        books.add(new EntryBuilder(values).toEntry());
    }

    public void add(String book, float newpagevalue, String newlinkvalue, WritingStatus newwsvalue, LocalDateTime newlrvalue, Booktype booktype, List<Genre> genres) {
        EntryBuilder eb = new EntryBuilder().setName(book)
                                            .setReadto(newpagevalue)
                                            .setLink(newlinkvalue)
                                            .setWs(newwsvalue)
                                            .setLastread(newlrvalue).setBooktype(booktype).setGenres(genres);
        books.add(eb.toEntry());
    }

    public void addAll(List<Object> books) {
        for (Object book : books) {
            EntryBuilder eb = new EntryBuilder((Map<String, Object>) book);
            this.books.add(eb.toEntry());
        }
    }

    public Entry get(String s) {
        return books.stream().filter(a -> a.name().equals(s)).findAny().orElse(null);
    }

    public Stream<Entry> entries() {
        return books.stream();
    }

    public List<Entry> list() {
        return books;
    }
}
