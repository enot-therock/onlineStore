package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UpdateUser;
import ru.skypro.homework.model.dto.UserDTO;
import ru.skypro.homework.model.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Маппим из Entity в DTO
     */

    @Mapping(target = "id", expression = "java(user.getId().intValue())")
    @Mapping(source = "username", target = "email")
    UserDTO userToEntity(Users user);

    UpdateUser userResponseUpdate(Users user);

    @Mapping(source = "newPassword", target = "newPassword")
    NewPassword userSetPassword(Users users, String newPassword);

    /**
     * Маппим обратно и обновляем данные
     */

    @Mapping(target = "id", expression = "java((long) userDTO.getId())")
    @Mapping(source = "email", target = "username")
    Users toEntity(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "username", ignore = true)
    Users updateToEntity(UpdateUser updateUser);

}
