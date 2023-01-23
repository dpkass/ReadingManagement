package dpkass.readingmanagment.Domain.Aggregates.RequestParts;

import dpkass.readingmanagment.Domain.Types.Attribute;

import java.util.List;

public record ChangeInputOption(Attribute attribute, ChangeForm.ChangeAttributeType type, List<?> input) {
}
