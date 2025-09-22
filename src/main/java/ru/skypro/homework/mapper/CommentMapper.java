package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.model.dto.CommentDTO;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Маппим из Entity в DTO
     */

    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "user.firstName", target = "authorFirsName")
    @Mapping(source = "user.image", target = "authorImage")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "stringToInt" )
    CommentDTO commentToEntity(Comment comment);

    CreateOrUpdateComment updateComment(Comment comment);

    /**
     * Маппим обратно и обновляем данные
     */

    Comment comment(CreateOrUpdateComment updateComment);

    @Named("stringToInt")
    static int stringToInt(String value) {
        if (value != null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
