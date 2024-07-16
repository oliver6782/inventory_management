package com.project.inventory_management.mapper;

import com.project.inventory_management.dto.UserResponseDTO;
import com.project.inventory_management.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

// maps user to userDTO that requests can get, password is not returned to request
@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "role", expression = "java(user.getRole().toString())")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    UserResponseDTO toUserResponseDTO(User user);
}
