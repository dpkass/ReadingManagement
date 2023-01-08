package AppRunner.Datacontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DisplayAttributesForm {

    List<Attribute> displayed = new ArrayList<>();

    public DisplayAttributesForm(List<String> list) {
        List<String[]> errors = new ArrayList<>();
        for (String s : list) {
            if (s == null) continue;

            Attribute att = Attribute.getAttribute(s);
            if (att == null) errors.add(new String[] { "displayattribute", "displayattnotatt", s + " is not a displayattribute" });
            else displayed.add(att);
        }
        if (!errors.isEmpty()) throw new RequestParsingException(errors);
    }

    public Stream<Attribute> stream() {
        return displayed.stream().sorted();
    }
}
