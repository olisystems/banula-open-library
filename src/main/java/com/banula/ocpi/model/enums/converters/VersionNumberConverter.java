package com.banula.ocpi.model.enums.converters;

import com.banula.ocpi.model.enums.VersionNumber;
import org.springframework.core.convert.converter.Converter;

public class VersionNumberConverter implements Converter<String, VersionNumber> {
    @Override
    public VersionNumber convert(String source) {
        return VersionNumber.fromValue(source.toUpperCase());
    }
}
