package AppRunner.Datastructures;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// rework OR filters
public record Filter<T>(Attribute attribute, String operator, T value) {
    public static Filter<?> createFilter(String[] filter) {
        Attribute att = Attribute.getAttribute(filter[0]);
        String operator = filter[1];
        if (att == null) throw new IllegalArgumentException("1");
        return switch (att) {
            case readto, rating -> new Filter<>(att, operator, Float.parseFloat(filter[2]));
            case writingStatus, readingStatus -> new Filter<>(att, operator, filter[2]);
            case lastread, pauseduntil ->
                    new Filter<LocalDate>(att, operator, LocalDate.parse(filter[2], DateTimeFormatter.ofPattern("dd.MM.yyyy")));
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

    public String[] toArray() {
        return new String[] { String.valueOf(attribute), operator, String.valueOf(value) };
    }
}
