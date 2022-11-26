package AppRunner.Datastructures;

import java.util.Arrays;
import java.util.List;

public enum Operator {
    New("New"), Read("Read"), ReadTo("Read To"), Add("Add"), Change("Change"), Open("Open"), Recommend("Recommend"), Show("Show"), List("List"), ListAll("List All"), Help("Help");

    private final String displayvalue;

    public static List<Operator> formoperators() {
        return Arrays.stream(values()).filter(f -> f != Help).toList();
    }

    public static Operator getOperator(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    Operator(String displayvalue) {this.displayvalue = displayvalue;}

    public String displayvalue() {
        return displayvalue;
    }
}
