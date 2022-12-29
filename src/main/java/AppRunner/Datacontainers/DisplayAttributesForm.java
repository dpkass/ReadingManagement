package AppRunner.Datacontainers;

import java.util.List;

public record DisplayAttributesForm(boolean displayread, boolean displaylink, boolean displayrating, boolean displaylastread, boolean displaywaituntil, boolean displayreadingstatus, boolean displaywritingstatus) {

    public DisplayAttributesForm(List<Boolean> list) {
        this(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6));
    }

    public static DisplayAttributesForm all() {
        return new DisplayAttributesForm(true, true, true, true, true, true, true);
    }

    public static DisplayAttributesForm none() {
        return new DisplayAttributesForm(false, false, false, false, false, false, false);
    }

    public List<Boolean> asList() {
        return List.of(displayread, displaylink, displayrating, displaylastread, displaywaituntil, displayreadingstatus,
                displaywritingstatus);
    }

    public boolean isEmpty() {
        return !(displayread || displaylink || displayrating || displaylastread || displaywaituntil || displayreadingstatus || displaywritingstatus);
    }
}
