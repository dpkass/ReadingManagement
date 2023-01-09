package AppRunner.Data.Types;

import java.util.List;

public enum Operator {
    Secret("Secret"), New("New"), Read("Read"), ReadTo("Read To"), Add("Add"), Change("Change"), Open("Open"), Recommend("Recommend"), List("List"), Wait("Wait"), Pause("Pause"), Help("Help");

    private final String displayvalue;

    public static List<Operator> formoperators() {
        return java.util.List.of(New, Read, ReadTo, Change, Open, Recommend, List, Add, Wait, Pause);
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
            case "s", "secret" -> null;
            default -> throw new IllegalArgumentException();
        };
    }

    Operator(String displayvalue) {this.displayvalue = displayvalue;}

    public String displayvalue() {
        return displayvalue;
    }
}
