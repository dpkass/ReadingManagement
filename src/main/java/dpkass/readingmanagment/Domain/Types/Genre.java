package dpkass.readingmanagment.Domain.Types;

public enum Genre {
    Action, Adventure, Romance, Shonen("Shōnen"), Seinen, Comedy, Isekai, Harem, SoL("Slice of Life"), Mecha, SF("Science Fiction"),
    Drama, Fantasy, Utopia, Dystopia, Supernatural;

    final String displayvalue;

    Genre() {
        this.displayvalue = this.toString();
    }

    Genre(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public String displayvalue() {
        return displayvalue;
    }

    public static Genre getGenre(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
