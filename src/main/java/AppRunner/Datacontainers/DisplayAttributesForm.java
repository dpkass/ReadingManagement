package AppRunner.Datacontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static AppRunner.Datacontainers.Attribute.*;

public record DisplayAttributesForm(boolean displayread, boolean displaylink, boolean displayrating, boolean displaylastread, boolean displaywaituntil, boolean displayreadingstatus, boolean displaywritingstatus) {

    public DisplayAttributesForm(List<Boolean> list) {
        this(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6));
    }

    public static DisplayAttributesForm build(List<String> subList) {
        boolean displayread = false, displaylink = false, displayrating = false, displaylastread = false, displaywaituntil = false, displayreadingstatus = false, displaywritingstatus = false;

        List<String[]> errors = new ArrayList<>();
        for (String s : subList) {
            Attribute att = Attribute.getAttribute(s);
            if (att == null) errors.add(new String[] { "displayattribute", "displayattnotatt", s + " is not a displayattribute" });
            else switch (att) {
                case readto -> displayread = true;
                case link -> displaylink = true;
                case rating -> displayrating = true;
                case lastread -> displaylastread = true;
                case waituntil -> displaywaituntil = true;
                case readingStatus -> displayreadingstatus = true;
                case writingStatus -> displaywritingstatus = true;
            }
        }
        if (!errors.isEmpty()) throw new RequestParsingException(errors);

        return new DisplayAttributesForm(displayread, displaylink, displayrating, displaylastread, displaywaituntil, displayreadingstatus, displaywritingstatus);
    }

    public Stream<Attribute> stream() {
        List<Attribute> enabledList = new ArrayList<>();

        if (displayread) enabledList.add(readto);
        if (displaylastread) enabledList.add(lastread);
        if (displayreadingstatus) enabledList.add(readingStatus);
        if (displaywaituntil) enabledList.add(waituntil);
        if (displaywritingstatus) enabledList.add(writingStatus);
        if (displayrating) enabledList.add(rating);
        if (displaylink) enabledList.add(link);

        return enabledList.stream();
    }
}
