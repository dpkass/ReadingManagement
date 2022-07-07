package EntryHandling;

public record Entry(String name, int chapter, String link) {
    public Entry(String[] values) {
        this(values[0], Integer.parseInt(values[1]), values[2]);
    }

    public String toCSV() {
        return name + ", " + chapter + ", " + link;
    }
}
