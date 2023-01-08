package Management;

import java.time.format.DateTimeFormatter;

public class Helper {
    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    static String errorMessage(String error) {
        return switch (error) {
            case "1" -> "Invalid Input. Use help for more info.";
            case "2" -> "The given book is already in the list.";
            case "3" -> "The given book was not found. If you want to add a new Entry use \"new\".";
            case "4" -> "The read-to value of the given book is not a number. Use command read-to to adjust.";
            case "5" ->
                    "The provided link seems to be wrong. Please correct the link with \"change link\" and try again. For more info on how to change link use \"help change\".\nThe provided link is:";
            case "6" -> "Your OS does not support opening a link. Copy the following link and paste it to a browser of your choosing:\n";
            default -> error;
        };
    }

    public static String representation(String part) {
        return switch (part) {
            // order
            case "sortby", "sortBy", "sort", "sb" -> "sb";
            case "groupby", "groupBy", "group", "gb" -> "gb";
            case "descending", "desc", "d" -> "desc";
            default -> part;
        };
    }
}