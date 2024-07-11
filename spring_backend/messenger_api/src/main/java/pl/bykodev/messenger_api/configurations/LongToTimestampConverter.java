package pl.bykodev.messenger_api.configurations;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Converter(autoApply = true)
public class LongToTimestampConverter implements AttributeConverter<Long, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(Long aLong) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(aLong), ZoneId.systemDefault());
    }

    @Override
    public Long convertToEntityAttribute(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
