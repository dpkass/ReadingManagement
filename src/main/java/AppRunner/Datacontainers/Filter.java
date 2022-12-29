package AppRunner.Datacontainers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;

// rework OR filters
public record Filter<T>(Attribute attribute, String operator, T value) {
    public static Filter<?> createFilter(String[] filter) {
        Attribute att = Attribute.getAttribute(filter[0]);
        Object value = (att == Attribute.writingStatus || att == Attribute.readingStatus) ? getStatusValue(filter[2]) : filter[2];
        return createFilter(att, filter[1], value);
    }

    public static <T> Filter<?> createFilter(Attribute att, String operator, T value) {
        if (att == null) throw new IllegalArgumentException("1");
        return switch (att) {
            case name -> new Filter<>(att, operator, (String) value);
            case readto, rating -> new Filter<>(att, operator, value);
            case writingStatus, readingStatus -> new Filter<>(att, operator, ((Collection<String>) value));
            case lastread, waituntil -> new Filter<LocalDate>(att, operator, LocalDate.parse((String) value, DateTimeFormatter.ISO_DATE));
            default -> throw new IllegalArgumentException("1");
        };
    }

    private static Set<String> getStatusValue(String value) {
        return Set.of(value.split("OR"));
    }

    public boolean isComplexFilter() {
        return value instanceof Collection<?> col && col.size() > 1;
    }
}
