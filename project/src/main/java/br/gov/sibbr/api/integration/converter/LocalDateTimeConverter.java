package br.gov.sibbr.api.integration.converter;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return attribute != null ? Timestamp.valueOf(attribute) : null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp data) {
        return data != null ? data.toLocalDateTime() : null;
    }
}