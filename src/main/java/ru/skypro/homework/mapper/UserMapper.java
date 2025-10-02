package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.skypro.homework.model.dto.UpdateUser;
import ru.skypro.homework.model.dto.UserDTO;
import ru.skypro.homework.model.entity.Image;
import ru.skypro.homework.model.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Маппим из Entity в DTO
     */

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "image", target = "image", qualifiedByName = "mapUserImageToUrl")
    UserDTO toUserDTO(Users user);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    UpdateUser toUpdateUserDTO(Users user);

    /**
     * Маппим обратно и обновляем данные
     */

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    void updateUserFromDTO(UpdateUser updateUserDTO, @MappingTarget Users user);

    /**
     * доп методы для преобразования Image в строку и обратно
     */

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "username")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "image", target = "image", qualifiedByName = "mapUrlToUserImage")
    Users toUserEntity(UserDTO userDTO);

    @Named("mapUserImageToUrl")
    default String mapUserImageToUrl(Image image) {
        if (image == null) {
            return null;
        }
        return "/users/me/image";
    }

    @Named("mapUrlToUserImage")
    default Image mapUrlToUserImage(String filePath) {
        if (filePath == null) return null;
        Image image = new Image();
        image.setFilePath(filePath);
        return image;
    }
}
