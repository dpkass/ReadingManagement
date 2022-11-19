package EntryHandling.Entry;

import EntryHandling.Entry.Status.ReadingStatus;
import EntryHandling.Entry.Status.WritingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Entry {
    private String name;
    private double readto;
    private String link;

    private WritingStatus writingStatus;
    private ReadingStatus readingStatus;

    @JsonFormat (pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime lastread;
    
    @JsonFormat (pattern = "dd.MM.yyyy")
    private LocalDate pauseduntil;

    private List<String> abbreviations;

    public Entry(String name, double readto, String link, WritingStatus writingStatus, ReadingStatus readingStatus, LocalDateTime lastread, LocalDate pauseduntil, List<String> abbreviations) {
        this.name = name;
        this.readto = readto;
        this.link = link;
        this.writingStatus = writingStatus;
        this.readingStatus = readingStatus;
        this.lastread = lastread;
        this.pauseduntil = pauseduntil;
        this.abbreviations = abbreviations;
    }

    // getter
    public String name() {
        return name;
    }

    public double readto() {
        return readto;
    }

    public String link() {
        return link;
    }

    public LocalDateTime lastread() {
        return lastread;
    }

    public LocalDate pauseduntil() {
        return pauseduntil;
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

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setReadto(double rt) {
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

    public void setPauseduntil(LocalDate pauseduntil) {
        this.pauseduntil = pauseduntil;
    }

    public void setAbbreviations(List<String> abbreviations) {
        this.abbreviations = abbreviations;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = ReadingStatus.getStatus(readingStatus);
        if (this.readingStatus == ReadingStatus.Default) throw new RuntimeException();
    }

    public void setWritingStatus(String writingStatus) {
        this.writingStatus = WritingStatus.getStatus(writingStatus);
        if (this.writingStatus == WritingStatus.Default) throw new RuntimeException();
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
