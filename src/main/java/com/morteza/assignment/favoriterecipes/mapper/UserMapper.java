package com.morteza.assignment.favoriterecipes.mapper;

import com.morteza.assignment.favoriterecipes.dto.UserDto;
import com.morteza.assignment.favoriterecipes.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User recipe);

    User toEntity(UserDto recipe);
}
