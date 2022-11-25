package AppRunner.Datastructures;

import java.util.Arrays;
import java.util.Objects;

public enum Operator {
    NEW("New"), READ("Read"), READTO("Read To"), ADD("Add"), CHANGE("Change"), OPEN("Open"), RECOMMEND("Recommend"), SHOW("Show"), LIST("List"), LISTALL("List All");

    private final String displayvalue;

    Operator(String displayvalue) {this.displayvalue = displayvalue;}

    public String displayvalue() {
        return displayvalue;
    }

    public static Operator getWithDisplayValue(String displayValue) {
        return Arrays.stream(values()).filter(e -> Objects.equals(e.displayvalue(), displayValue)).findAny().orElse(null);
    }
}
