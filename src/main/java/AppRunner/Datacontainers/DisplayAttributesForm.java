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
                case ReadTo -> displayread = true;
                case Link -> displaylink = true;
                case Rating -> displayrating = true;
                case LastRead -> displaylastread = true;
                case WaitUntil -> displaywaituntil = true;
                case ReadingStatus -> displayreadingstatus = true;
                case WritingStatus -> displaywritingstatus = true;
            }
        }
        if (!errors.isEmpty()) throw new RequestParsingException(errors);

        return new DisplayAttributesForm(displayread, displaylink, displayrating, displaylastread, displaywaituntil, displayreadingstatus, displaywritingstatus);
    }

    public Stream<Attribute> stream() {
        List<Attribute> enabledList = new ArrayList<>();

        if (displayread) enabledList.add(ReadTo);
        if (displaylastread) enabledList.add(LastRead);
        if (displayreadingstatus) enabledList.add(ReadingStatus);
        if (displaywaituntil) enabledList.add(WaitUntil);
        if (displaywritingstatus) enabledList.add(WritingStatus);
        if (displayrating) enabledList.add(Rating);
        if (displaylink) enabledList.add(Link);

        return enabledList.stream();
    }
}
