package ru.tinkoff.edu.java.scrapper.model.mapping;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.TimeZone;

@Mapper(componentModel = "spring")
public interface TimeMapper {

    default OffsetDateTime map(Instant instant) {
        return instant == null ? null : OffsetDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId());
    }

    default Instant map(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toInstant();
    }

}
