package AppRunner.Datacontainers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static AppRunner.Datacontainers.Attribute.*;

public class DisplayAttributesUtil {

    public static Set<Attribute> displayattributes = Set.of(readto, link, rating, lastread, waituntil, writingStatus, readingStatus);

    public static Set<Attribute> enabledSet = Set.of();

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

    public static Stream<Attribute> stream(DisplayAttributesForm daf) {
        buildEnabledSet(daf);
        return enabledSet.stream();
    }

    private static void buildEnabledSet(DisplayAttributesForm daf) {
        if (daf == null) return;

        Set<Attribute> res = new HashSet<>();
        if (daf.displayread()) res.add(readto);
        if (daf.displaylink()) res.add(link);
        if (daf.displayrating()) res.add(rating);
        if (daf.displaylastread()) res.add(lastread);
        if (daf.displaywaituntil()) res.add(waituntil);
        if (daf.displayreadingstatus()) res.add(readingStatus);
        if (daf.displaywritingstatus()) res.add(writingStatus);
        enabledSet = res;
    }

    public static boolean isDisplayattribute(Attribute att) {
        return displayattributes.contains(att);
    }
}
