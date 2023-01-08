package EntryHandling.Entry;

import AppRunner.Datacontainers.Booktype;
import AppRunner.Datacontainers.Genre;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Entry {
    private String name;
    private float readto;
    private String link;

    private float rating;

    private WritingStatus writingStatus;
    private ReadingStatus readingStatus;

    @JsonFormat (pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime lastread;

    @JsonFormat (pattern = "dd.MM.yyyy")
    private LocalDate waituntil;

    private final List<Genre> genres;
    private Booktype booktype;

    public Entry(String name, float readto, String link, float rating, WritingStatus writingStatus, ReadingStatus readingStatus, LocalDateTime lastread, LocalDate waituntil, List<Genre> genres, Booktype booktype) {
        this.name = name;
        this.readto = readto;
        this.link = link;
        this.rating = rating;
        this.writingStatus = writingStatus;
        this.readingStatus = readingStatus;
        this.lastread = lastread;
        this.waituntil = waituntil;
        this.genres = genres;
        this.booktype = booktype;
    }

    // getter
    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float readto() {
        return readto;
    }

    public void setReadto(float readto) {
        this.readto = readto;
    }

    public String link() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float rating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public WritingStatus writingStatus() {
        return writingStatus;
    }

    public void setWritingStatus(WritingStatus writingStatus) {
        this.writingStatus = writingStatus;
    }

    public ReadingStatus readingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(ReadingStatus readingStatus) {
        this.readingStatus = readingStatus;
    }

    public LocalDateTime lastread() {
        return lastread;
    }

    public void setLastread(LocalDateTime lastread) {
        this.lastread = lastread;
    }

    public LocalDate waituntil() {
        return waituntil;
    }

    public void setWaituntil(LocalDate waituntil) {
        this.waituntil = waituntil;
    }

    public List<Genre> genres() {
        return genres;
    }

    public Booktype booktype() {
        return booktype;
    }

    public void setBooktype(Booktype booktype) {
        this.booktype = booktype;
    }

    // special setters
    public void setWritingStatus(String writingStatus) {
        WritingStatus temp = WritingStatus.getStatus(writingStatus);
        if (temp == WritingStatus.Default) throw new RuntimeException();
        this.writingStatus = temp;
    }

    public void setBooktype(String booktype) {
        Booktype temp = Booktype.getBooktype(booktype);
        if (temp == null) throw new RuntimeException();
        this.booktype = temp;
    }

    public void addRead(double read) {
        this.readto += read;
    }

    @Override
    public boolean equals(Object obj) {
        return equals((Entry) obj);
    }

    public boolean equals(Entry e) {
        return Objects.equals(e.name, this.name);
    }
}
