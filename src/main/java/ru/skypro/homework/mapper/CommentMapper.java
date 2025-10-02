package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.model.dto.CommentDTO;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.entity.Comment;
import ru.skypro.homework.model.entity.Image;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Маппим из Entity в DTO
     */

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "user.image", target = "authorImage", qualifiedByName = "mapUserImageToUrl")
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapInstantToMillis")
    @Mapping(source = "text", target = "text")
    CommentDTO toCommentDTO(Comment comment);

    @Mapping(source = "text", target = "text")
    CreateOrUpdateComment toCreateOrUpdateCommentDTO(Comment comment);

    /**
     * Маппим обратно и обновляем данные
     */

    @Mapping(source = "text", target = "text")
    Comment toComment(CreateOrUpdateComment createOrUpdateCommentDTO);

    /**
     * методы для преобразования Instant в Long и обратно
     */

    @Named("mapInstantToMillis")
    default long mapInstantToMillis(Instant instant) {
        if (instant == null) {
            return 0L;
        }
        return instant.toEpochMilli();
    }

    @Named("mapUserImageToUrl")
    default String mapUserImageToUrl(Image image) {
        if (image == null) {
            return null;
        }
        return "/users/image/" + image.getId();
    }
}
