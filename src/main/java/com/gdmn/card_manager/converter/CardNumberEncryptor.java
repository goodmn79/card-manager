package com.gdmn.card_manager.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;

@Converter
@RequiredArgsConstructor
public class CardNumberEncryptor implements AttributeConverter<String, String> {
    private final StringEncryptor encryptor;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return encryptor.encrypt(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt card number", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return encryptor.decrypt(dbData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt card number", e);
        }
    }
}
