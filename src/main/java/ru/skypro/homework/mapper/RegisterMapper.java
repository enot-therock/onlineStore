package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.model.entity.Users;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    Users usersRegister(Register userResponseRegister);
}
