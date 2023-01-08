package AppRunner.Datacontainers;

public enum Booktype {
    Manga, Manhwa, Manhua, Webtoon, Comic, Lightnovel, Novel;

    public static Booktype getBooktype(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
