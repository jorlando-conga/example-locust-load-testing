package example.todolist.data.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime entityAttribute) {
        return toTimestamp(entityAttribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestampAttribute) {
        return fromTimestamp(timestampAttribute);
    }

    public static Timestamp toTimestamp(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return Timestamp.valueOf(ldt);
    }

    public static LocalDateTime fromTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }
}