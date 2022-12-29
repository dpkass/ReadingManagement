package AppRunner.Datastructures;

import java.util.Arrays;
import java.util.List;

public enum Operator {
    New("New"), Read("Read"), ReadTo("Read To"), Add("Add"), Change("Change"), Open("Open"), Recommend("Recommend"), List("List"), Help("Help"), Wait("Wait"), Pause("Pause");

    private final String displayvalue;

    public static List<Operator> formoperators() {
        return Arrays.stream(values()).filter(f -> f != Help).toList();
    }

    public static Operator getOperator(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException iae) {
            try {
                return representation(s);
            } catch (IllegalArgumentException iae2) {
                return null;
            }
        } catch (NullPointerException npe) {
            return null;
        }
    }

    static Operator representation(String s) {
        return switch (s) {
            case "n" -> New;
            case "r" -> Read;
            case "rt" -> ReadTo;
            case "a" -> Add;
            case "c" -> Change;
            case "o" -> Open;
            case "rec" -> Recommend;
            case "l" -> List;
            case "h" -> Help;
            default -> throw new IllegalArgumentException();
        };
    }

    Operator(String displayvalue) {this.displayvalue = displayvalue;}

    public String displayvalue() {
        return displayvalue;
    }
}
