package AppRunner.Datastructures;

import java.util.List;

public record DisplayAttributesForm(boolean displayread, boolean displaylink, boolean displayrating, boolean displaylastread, boolean displaypauseduntil, boolean displayreadingstatus, boolean displaywritingstatus) {

    public DisplayAttributesForm(List<Boolean> list) {
        this(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6));
    }

    public static DisplayAttributesForm all() {
        return new DisplayAttributesForm(true, true, true, true, true, true, true);
    }

    public List<Boolean> asList() {
        return List.of(displayread, displaylink, displayrating, displaylastread, displaypauseduntil, displayreadingstatus,
                displaywritingstatus);
    }

    public boolean isEmpty() {
        return !(displayread || displaylink || displayrating || displaylastread || displaypauseduntil || displayreadingstatus || displaywritingstatus);
    }
}
