package com.example.tinylink.config;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Convert java.util.Date -> OffsetDateTime when reading from Mongo.
 */
public class DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(Date source) {
        if (source == null) return null;
        Instant instant = source.toInstant();
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
