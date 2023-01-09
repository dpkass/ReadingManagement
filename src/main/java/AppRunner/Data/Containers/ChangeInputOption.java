package AppRunner.Data.Containers;

import AppRunner.Data.Types.Attribute;

import java.util.List;

public record ChangeInputOption(Attribute attribute, ChangeForm.ChangeAttributeType type, List<?> input) {
}
