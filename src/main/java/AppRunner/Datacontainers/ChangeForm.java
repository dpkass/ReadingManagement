package AppRunner.Datacontainers;

import EntryHandling.Entry.WritingStatus;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static AppRunner.Datacontainers.ChangeForm.ChangeAttributeType.*;

public class ChangeForm {

    public enum ChangeAttributeType {
        Text, Number, List
    }

    static List<ChangeInputOption> inputOptions = new ArrayList<>();

    static {
        List<Attribute> attributes = Attribute.changingOptions();

        for (Attribute attribute : attributes) {
            ChangeAttributeType type;
            List<?> value = null;

            switch (attribute) {
                case Name, Link -> type = Text;
                case StoryRating, Rating, CharactersRating, DrawingRating -> type = Number;
                case WritingStatus -> {
                    value = WritingStatus.selectableWS();
                    type = List;
                }
                case Booktype -> {
                    value = Booktype.selectableBooktypes();
                    type = List;
                }
                default -> throw new IllegalStateException();
            }
            inputOptions.add(new ChangeInputOption(attribute, type, value));
        }
    }

    Map<Attribute, Object> changeMap = new LinkedHashMap<>();

    public ChangeForm(List<ChangeValueWrapper> changevalues) {
        List<Attribute> entries = Attribute.changingOptions();

        for (int i = 0; i < changevalues.size(); i++) {
            ChangeValueWrapper valwrapper = changevalues.get(i);
            if (valwrapper == null) continue;
            Object val = valwrapper.getValue();
            Attribute att = entries.get(i);
            if (val == null || (val instanceof String strval && strval.isBlank())) continue;
            changeMap.put(att, switch (att) {
                case Name, Link -> (String) val;
                case StoryRating, Rating, CharactersRating, DrawingRating -> (Float) val;
                case WritingStatus -> WritingStatus.getStatus((String) val);
                case Booktype -> Booktype.getBooktype((String) val);
                default -> throw new IllegalStateException("Unexpected value: " + att);
            });
        }
    }

    public static List<ChangeValueWrapper> initialvalues() {
        return IntStream.range(0, Attribute.changingOptions().size())
                        .mapToObj(i -> new ChangeValueWrapper("")).collect(Collectors.toList());
    }

    public static List<ChangeInputOption> inputOptions() {
        return inputOptions;
    }

    public Map<Attribute, Object> changeMap() {
        return changeMap;
    }
}
