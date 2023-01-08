package EntryHandling.Entry;

import AppRunner.Datacontainers.Booktype;
import AppRunner.Datacontainers.Genre;
import org.json.JSONArray;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static EntryHandling.Entry.ReadingStatus.*;

public class EntryBuilder {
    String name;
    float readto = 0f;

    float rating = -1f;
    float storyrating = -1f;
    float charactersrating = -1f;
    float drawingrating = -1f;

    String link = "";
    
    WritingStatus ws = WritingStatus.Default;
    ReadingStatus rs = ReadingStatus.Default;

    LocalDateTime lastread = LocalDateTime.now();
    LocalDate waituntil;

    private List<Genre> genres = List.of();
    private Booktype booktype = Booktype.Default;

    public EntryBuilder(List<String> values) {
        name = values.get(0);
        if (values.size() > 1) readto = parseReadTo(values.get(1));
        if (values.size() > 2) link = values.get(2);
        if (values.size() > 3) ws = WritingStatus.getStatus(values.get(3));
        if (values.size() > 4) lastread = toLDT(values.get(4));
    }

    public EntryBuilder(Map<String, Object> JSONmap) {
        for (Map.Entry<String, Object> entry : JSONmap.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> name = (String) entry.getValue();
                case "readto" -> readto = ((BigDecimal) entry.getValue()).floatValue();
                case "rating" -> rating = ((BigDecimal) entry.getValue()).floatValue();
                case "storyrating" -> storyrating = ((BigDecimal) entry.getValue()).floatValue();
                case "charactersrating" -> charactersrating = ((BigDecimal) entry.getValue()).floatValue();
                case "drawingrating" -> drawingrating = ((BigDecimal) entry.getValue()).floatValue();
                case "link" -> link = (String) entry.getValue();
                case "lastread" -> lastread = toLDT((String) entry.getValue());
                case "waituntil" -> waituntil = toLD((String) entry.getValue());
                case "writingStatus" -> ws = WritingStatus.getStatus((String) entry.getValue());
                case "readingStatus" -> rs = ReadingStatus.getStatus((String) entry.getValue());
                case "genres" -> genres = ((JSONArray) entry.getValue()).toList()
                                                                        .stream()
                                                                        .map(String.class::cast)
                                                                        .map(Genre::getGenre)
                                                                        .collect(Collectors.toList());
                case "booktype" -> booktype = Booktype.getBooktype((String) entry.getValue());
            }
        }
    }

    public EntryBuilder() {}

    private void chooseStatus() {
        if (rs == Waiting && waituntil == null) rs = Reading;      // autonullify paused when date arrives
        if (rs != ReadingStatus.Default && rs != null) return;

        double rt = readto;
        if (rt == 0) {
            rs = NotStarted;
            lastread = null;
        } else if (rt <= 5) {
            rs = Started;
        } else rs = Reading;
    }

    private LocalDateTime toLDT(String value) {
        if (value == null) return null;
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException dtpe) {
            return LocalDateTime.now();
        }
    }

    private LocalDate toLD(String value) {
        if (value == null) return null;
        try {
            LocalDate ld = LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            return ld.isBefore(LocalDate.now()) ? null : ld;
        } catch (DateTimeParseException dtpe) {
            return null;
        }
    }

    private float parseReadTo(String rt) {
        try {
            return Float.parseFloat(rt);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("4");
        }
    }

    public EntryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public EntryBuilder setReadto(float readto) {
        this.readto = readto;
        return this;
    }

    public EntryBuilder setLink(String link) {
        this.link = link;
        return this;
    }

    public EntryBuilder setWs(WritingStatus ws) {
        this.ws = ws;
        return this;
    }

    public EntryBuilder setLastread(LocalDateTime lastread) {
        this.lastread = lastread;
        return this;
    }

    public EntryBuilder setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public EntryBuilder setBooktype(Booktype booktype) {
        this.booktype = booktype;
        return this;
    }

    public Entry toEntry() {
        chooseStatus();
        return new Entry(name, readto, link, rating, storyrating, charactersrating, drawingrating, ws, rs, lastread, waituntil, genres, booktype);
    }
}
