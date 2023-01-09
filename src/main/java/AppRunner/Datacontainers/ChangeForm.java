package AppRunner.Datacontainers;

import EntryHandling.Entry.WritingStatus;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChangeForm {

    public enum ChangeAttributeType {
        Text, Number, List;

        public static List<ChangeAttributeType> getTypes(List<Attribute> changingOptions) {
            List<ChangeAttributeType> types = new ArrayList<>();
            for (Attribute co : changingOptions) {
                types.add(
                        switch (co) {
                            case Name, Link -> Text;
                            case Booktype, WritingStatus -> List;
                            case StoryRating, Rating, CharactersRating, DrawingRating -> Number;
                            default -> throw new IllegalStateException();
                        });
            }
            return types;
        }
    }

    static List<ChangeAttributeType> CATypes = ChangeAttributeType.getTypes(Attribute.changingOptions());
    static Map<Attribute, Object> changeAttributeToInputoptions = new LinkedHashMap<>();

    static {
        List<Attribute> attributes = Attribute.changingOptions();

        for (Attribute attribute : attributes) {
            changeAttributeToInputoptions.put(attribute, switch (attribute) {
                case Name, Link -> "";
                case StoryRating, Rating, CharactersRating, DrawingRating -> -1;
                case WritingStatus -> WritingStatus.selectableWS();
                case Booktype -> Booktype.selectableBooktypes();
                default -> throw new IllegalStateException();
            });
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

    public static List<ChangeAttributeType> CATypes() {
        return CATypes;
    }

    public static Map<Attribute, Object> changeAttributeToInputoptions() {
        return changeAttributeToInputoptions;
    }

    public Map<Attribute, Object> changeMap() {
        return changeMap;
    }
}
