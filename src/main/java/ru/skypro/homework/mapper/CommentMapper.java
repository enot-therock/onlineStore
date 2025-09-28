package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.model.dto.CommentDTO;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.entity.Comment;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Маппим из Entity в DTO
     */

    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "user.firstName", target = "authorFirsName")
    @Mapping(source = "user.image", target = "authorImage")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "instantToLong" )
    CommentDTO commentToEntity(Comment comment);

    CreateOrUpdateComment updateComment(Comment comment);

    /**
     * Маппим обратно и обновляем данные
     */

    Comment comment(CreateOrUpdateComment updateComment);

    /**
     * методы для преобразования Instant в Long и обратно
     */

    @Named("instantToLong")
    default Long instantToLong(Instant instant) {
        return instant != null ? instant.toEpochMilli() : null;
    }

    @Named("longToInstant")
    default Instant longToInstant(Long epochMillis) {
        return epochMillis != null ? Instant.ofEpochMilli(epochMillis) : null;
    }
}
