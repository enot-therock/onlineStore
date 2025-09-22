package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.dto.Login;
import ru.skypro.homework.model.entity.Users;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "phone", ignore = true)
    Users usersLogin(Login userLoginResponse);
}
