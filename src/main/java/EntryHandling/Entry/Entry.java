package EntryHandling.Entry;

import AppRunner.Data.Types.Booktype;
import AppRunner.Data.Types.Genre;
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
    private float storyrating;
    private float charactersrating;
    private float drawingrating;

    private WritingStatus writingStatus;
    private ReadingStatus readingStatus;

    @JsonFormat (pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime lastread;

    @JsonFormat (pattern = "dd.MM.yyyy")
    private LocalDate waituntil;

    private List<Genre> genres;
    private Booktype booktype;

    public Entry(String name, float readto, String link, float rating, float storyrating, float charactersrating, float drawingrating, WritingStatus writingStatus, ReadingStatus readingStatus, LocalDateTime lastread, LocalDate waituntil, List<Genre> genres, Booktype booktype) {
        this.name = name;
        this.readto = readto;
        this.link = link;
        this.rating = rating;
        this.storyrating = storyrating;
        this.charactersrating = charactersrating;
        this.drawingrating = drawingrating;
        this.writingStatus = writingStatus;
        this.readingStatus = readingStatus;
        this.lastread = lastread;
        this.waituntil = waituntil;
        this.genres = genres;
        this.booktype = booktype;
    }

    // getter & setter
    public String name() {
        return name;
    }

    public Entry setName(String name) {
        this.name = name;
        return this;
    }

    public float readto() {
        return readto;
    }

    public Entry setReadto(float readto) {
        this.readto = readto;
        return this;
    }

    public String link() {
        return link;
    }

    public Entry setLink(String link) {
        this.link = link;
        return this;
    }

    public float rating() {
        return rating;
    }

    public Entry setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public float storyrating() {
        return storyrating;
    }

    public Entry setStoryrating(float storyrating) {
        this.storyrating = storyrating;
        return this;
    }

    public float charactersrating() {
        return charactersrating;
    }

    public Entry setCharactersrating(float charactersrating) {
        this.charactersrating = charactersrating;
        return this;
    }

    public float drawingrating() {
        return drawingrating;
    }

    public Entry setDrawingrating(float drawingrating) {
        this.drawingrating = drawingrating;
        return this;
    }

    public WritingStatus writingStatus() {
        return writingStatus;
    }

    public Entry setWritingStatus(WritingStatus writingStatus) {
        this.writingStatus = writingStatus;
        return this;
    }

    public ReadingStatus readingStatus() {
        return readingStatus;
    }

    public Entry setReadingStatus(ReadingStatus readingStatus) {
        this.readingStatus = readingStatus;
        return this;
    }

    public LocalDateTime lastread() {
        return lastread;
    }

    public Entry setLastread(LocalDateTime lastread) {
        this.lastread = lastread;
        return this;
    }

    public LocalDate waituntil() {
        return waituntil;
    }

    public Entry setWaituntil(LocalDate waituntil) {
        this.waituntil = waituntil;
        return this;
    }

    public List<Genre> genres() {
        return genres;
    }

    public Entry setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public Booktype booktype() {
        return booktype;
    }

    public Entry setBooktype(Booktype booktype) {
        this.booktype = booktype;
        return this;
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
