package com.example.tinylink.config;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Convert OffsetDateTime -> java.util.Date for Mongo storage.
 */
public class OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {

    @Override
    public Date convert(OffsetDateTime source) {
        if (source == null) return null;
        return Date.from(source.toInstant());
    }
}
