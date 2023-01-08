package AppRunner.Datacontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static AppRunner.Datacontainers.Attribute.*;

public record DisplayAttributesForm(boolean displayread, boolean displaylink, boolean displayrating, boolean displaylastread, boolean displaywaituntil, boolean displayreadingstatus, boolean displaywritingstatus) {

    public static DisplayAttributesForm build(List<String> list) {
        boolean displayread = false, displaylink = false, displayrating = false, displaylastread = false, displaywaituntil = false, displayreadingstatus = false, displaywritingstatus = false;

        List<String[]> errors = new ArrayList<>();
        for (String s : list) {
            if (s == null) continue;

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
