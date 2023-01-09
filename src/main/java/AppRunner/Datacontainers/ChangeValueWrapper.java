package AppRunner.Datacontainers;

import AppRunner.Datacontainers.ChangeForm.ChangeAttributeType;

public class ChangeValueWrapper {
    float floatval;
    String stringval;
    ChangeAttributeType type;

    public ChangeValueWrapper(String val) {
        if ((floatval = isNumber(val)) != -1) {
            type = ChangeAttributeType.Number;
        } else {
            this.stringval = val;
            type = ChangeAttributeType.Text;
        }
    }

    private float isNumber(String val) {
        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public Object getValue() {
        if (type == null) return "";
        return switch (type) {
            case Text -> stringval;
            case Number -> floatval;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
