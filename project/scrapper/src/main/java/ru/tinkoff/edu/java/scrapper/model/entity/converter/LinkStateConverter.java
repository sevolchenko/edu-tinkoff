package ru.tinkoff.edu.java.scrapper.model.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.linkstate.ILinkState;
import ru.tinkoff.edu.java.scrapper.util.ObjectMapperUtil;

@Converter
public class LinkStateConverter implements AttributeConverter<ILinkState, String> {
    @Override
    public String convertToDatabaseColumn(ILinkState attribute) {
        return ObjectMapperUtil.writeValueAsString(attribute);
    }

    @Override
    public ILinkState convertToEntityAttribute(String dbData) {
        return ObjectMapperUtil.readValue(dbData, ILinkState.class);
    }
}
