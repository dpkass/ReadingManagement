package AppRunner.Datastructures;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// rework OR filters
public record Filter<T>(Attribute attribute, String operator, T value) {
    public static Filter<?> createFilter(String[] filter) {
        Attribute att = Attribute.getAttribute(filter[0]);
        String operator = filter[1];
        if (att == null) throw new IllegalArgumentException("1");
        return switch (att) {
            case readto, rating -> new Filter<>(att, operator, Float.parseFloat(filter[2]));
            case writingStatus, readingStatus -> new Filter<>(att, operator, filter[2]);
            case lastread, waituntil -> new Filter<LocalDate>(att, operator, LocalDate.parse(filter[2], DateTimeFormatter.ISO_DATE));
            default -> throw new IllegalArgumentException("1");
        };
    }

    public static <T> Filter<?> createFilter(Attribute att, String operator, T value) {
        if (att == null) throw new IllegalArgumentException("1");
        return switch (att) {
            case readto, rating -> new Filter<>(att, operator, value);
            case writingStatus, readingStatus -> new Filter<>(att, operator, String.join("OR", (List<String>) value));
            case lastread, waituntil -> new Filter<LocalDate>(att, operator, LocalDate.parse((String) value, DateTimeFormatter.ISO_DATE));
            default -> throw new IllegalArgumentException("1");
        };
    }

    public boolean isOrFilter() {
        if (!(value instanceof String)) return false;
        return ((String) value).split("OR").length > 1;
    }

    public String[] splitValue() {
        return ((String) value).split("OR");
    }
}
