package com.project.inventory_management.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.exception.MedicationTypeNotValidException;

import java.io.IOException;

public class TypeEnumDeserializer extends JsonDeserializer<Medication.Types> {
    @Override
    public Medication.Types deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String value = p.getText();
        try {
            return Medication.Types.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MedicationTypeNotValidException("Invalid value for enum type: " + value);
        }
    }
}
