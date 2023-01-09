package AppRunner.Datacontainers;

import java.util.List;

public enum Booktype {
    Default("-"), Manga, Manhwa, Manhua, Webtoon, Comic, Lightnovel, Novel;

    final String displayvalue;

    Booktype() {
        this.displayvalue = this.toString();
    }

    Booktype(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public static List<Booktype> selectableBooktypes() {
        return List.of(Manga, Manhwa, Manhua, Webtoon, Comic, Lightnovel, Novel);
    }

    public static Booktype getBooktype(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String displayvalue() {
        return displayvalue;
    }
}
