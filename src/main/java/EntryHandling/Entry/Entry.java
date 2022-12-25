package EntryHandling.Entry;

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

    private List<String> abbreviations;

    public Entry(String name, float readto, String link, float rating, WritingStatus writingStatus, ReadingStatus readingStatus, LocalDateTime lastread, LocalDate waituntil, List<String> abbreviations) {
        this.name = name;
        this.readto = readto;
        this.link = link;
        this.rating = rating;
        this.writingStatus = writingStatus;
        this.readingStatus = readingStatus;
        this.lastread = lastread;
        this.waituntil = waituntil;
        this.abbreviations = abbreviations;
    }

    // getter
    public String name() {
        return name;
    }

    public float readto() {
        return readto;
    }

    public String link() {
        return link;
    }

    public LocalDateTime lastread() {
        return lastread;
    }

    public LocalDate waituntil() {
        return waituntil;
    }

    public List<String> abbreviations() {
        return abbreviations;
    }

    public ReadingStatus readingStatus() {
        return readingStatus;
    }

    public WritingStatus writingStatus() {
        return writingStatus;
    }

    public float rating() {
        return rating;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setReadto(float rt) {
        this.readto = rt;
    }

    public void addRead(double read) {
        this.readto += read;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLastread(LocalDateTime lastread) {
        this.lastread = lastread;
    }

    public void setWaituntil(LocalDate waituntil) {
        this.waituntil = waituntil;
    }

    public void setAbbreviations(List<String> abbreviations) {
        this.abbreviations = abbreviations;
    }

    public void setReadingStatus(ReadingStatus readingStatus) {
        this.readingStatus = readingStatus;
    }

    public void setWritingStatus(String writingStatus) {
        this.writingStatus = WritingStatus.getStatus(writingStatus);
        if (this.writingStatus == WritingStatus.Default) throw new RuntimeException();
    }

    public void setWritingStatus(WritingStatus writingStatus) {
        this.writingStatus = writingStatus;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void addAbbreviation(String abbreviation) {
        if (!abbreviations.contains(abbreviation)) abbreviations.add(abbreviation);
    }

    public void removeAbbreviation(String abbreviation) {
        abbreviations.remove(abbreviation);
    }

    @Override
    public String toString() {
        return EntryUtil.asCSV(this);
    }

    @Override
    public boolean equals(Object obj) {
        return equals((Entry) obj);
    }

    public boolean equals(Entry e) {
        return Objects.equals(e.name, this.name);
    }
}
