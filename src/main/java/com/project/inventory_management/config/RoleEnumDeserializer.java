package com.project.inventory_management.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.project.inventory_management.entity.User;
import com.project.inventory_management.exception.UserRoleNotValidException;

import java.io.IOException;

public class RoleEnumDeserializer extends JsonDeserializer<User.Roles> {
    @Override
    public User.Roles deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String value = p.getText();
        try {
            return User.Roles.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserRoleNotValidException("Invalid value for enum role: " + value);
        }
    }
}
