package AppRunner.Data.Types;

import AppRunner.Data.Containers.ChangeForm;
import EntryHandling.Entry.Entry;
import EntryHandling.Entry.EntryUtil;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Management.Helper.df;
import static Management.Helper.dtf;

public enum Attribute {
    Name,
    Genres,
    Booktype,
    ReadTo("Page/Chapter"),
    LastRead("Last Read"),
    ReadingStatus("Reading Status"),
    WaitUntil("Wait Until"),
    WritingStatus("Writing Status"),
    StoryRating("Story Rating"),
    CharactersRating("Characters Rating"),
    DrawingRating("Drawing Rating"),
    Rating,
    Link;

    final String displayvalue;

    Attribute() {
        this.displayvalue = this.toString();
    }

    Attribute(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    /**
     * Any change relates to {@link ChangeForm.ChangeAttributeType#getTypes(List)}.
     *
     * @return List of all changeable options in the change screen
     */
    public static List<Attribute> changingOptions() {
        return List.of(Name, Booktype, StoryRating, CharactersRating, DrawingRating, Rating, WritingStatus, Link);
    }

    public static List<Attribute> displayingOptions() {
        return List.of(Genres, Booktype, ReadTo, LastRead, ReadingStatus, WaitUntil, WritingStatus, StoryRating, CharactersRating, DrawingRating, Rating, Link);
    }

    public static List<Attribute> sortingOptions() {
        return List.of(Name, ReadTo, StoryRating, CharactersRating, DrawingRating, Rating, Booktype, LastRead, WaitUntil, WritingStatus, ReadingStatus);
    }

    public static List<Attribute> groupingOptions() {
        return List.of(ReadTo, StoryRating, CharactersRating, DrawingRating, Rating, Booktype, LastRead, WritingStatus, ReadingStatus);
    }

    public static Function<Entry, String> getFunction(Attribute att) {
        return e -> switch (att) {
            case Link -> e.link();
            case ReadTo -> EntryUtil.ratingString(e.readto());
            case StoryRating -> EntryUtil.ratingString(e.storyrating());
            case CharactersRating -> EntryUtil.ratingString(e.charactersrating());
            case DrawingRating -> EntryUtil.ratingString(e.drawingrating());
            case Rating -> EntryUtil.ratingString(e.rating());
            case LastRead -> EntryUtil.dateString(e.lastread(), dtf, "-");
            case WaitUntil -> EntryUtil.dateString(e.waituntil(), df, "-");
            case WritingStatus -> e.writingStatus().displayvalue();
            case ReadingStatus -> e.readingStatus().displayvalue();
            case Genres -> e.genres().stream().map(Genre::displayvalue).collect(Collectors.joining("\n"));
            case Booktype -> e.booktype().displayvalue();
            default -> throw new IllegalArgumentException("1");
        };
    }

    public static Attribute getAttribute(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    public String displayvalue() {
        return displayvalue;
    }
}
