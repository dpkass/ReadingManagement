package AppRunner.Datacontainers;

import java.util.List;

public record ChangeInputOption(Attribute attribute, ChangeForm.ChangeAttributeType type, List<?> input) {
}
