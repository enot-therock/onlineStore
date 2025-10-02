package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.excepption.ForbiddenException;
import ru.skypro.homework.excepption.NotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.dto.CommentDTO;
import ru.skypro.homework.model.dto.Comments;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.model.entity.Advertisement;
import ru.skypro.homework.model.entity.Comment;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.repository.AdvertisementRepository;
import ru.skypro.homework.repository.CommentRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UsersService usersService;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository,
                          AdvertisementRepository advertisementRepository,
                          UsersService usersService,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.advertisementRepository = advertisementRepository;
        this.usersService = usersService;
        this.commentMapper = commentMapper;
    }

    /**
     * метод для создания комментария
     * @param adsId - задаем ID объявления, к которому пишем коммент
     * @param commentDTO - DTO для создания коммента
     * comment - заполняем Entity входными данными
     * @return - возвращаем готовый DTO для ответа
     */

    public CommentDTO createComment(Long adsId, CommentDTO commentDTO) {
        Users currentUser = usersService.getCurrentUser();
        Advertisement advertisement = advertisementRepository.findById(adsId)
                .orElseThrow(() -> new NotFoundException("Advertisement is not found " + adsId));

        Comment comment = new Comment();
        comment.setUser(currentUser);
        comment.setCreatedAt(Instant.ofEpochMilli(commentDTO.getCreatedAt()));
        comment.setAdvertisement(advertisement);
        comment.setText(commentDTO.getText());

        Comment createComment = commentRepository.save(comment);
        return commentMapper.toCommentDTO(createComment);
    }

    /**
     * метод получения все комментов к конктретному объявлению
     * @param adsId - ID соответствующего объявления
     * @return - возвращаем необходимое DTO для ответа
     */

    public Comments getAllComments(Long adsId) {
        if (!advertisementRepository.existsById(adsId)) {
            throw new NotFoundException("Advertisement is not found " + adsId);
        }
        List<Comment> comments = commentRepository.findByAdvertisementIdOrderByCreatedAtDesc(adsId);
        List<CommentDTO> commentDTOS = comments.stream()
                .map(commentMapper::toCommentDTO)
                .collect(Collectors.toList());
        return new Comments(commentDTOS.size(), commentDTOS);
    }

    /**
     * метод для удаления комментариев
     * @param adsId - ID объявления, у которого необходимо удалить коммент
     * @param commentId - ID нужного коммента
     */

    public void deleteComment(Long adsId, Long commentId) {
        Comment comment = commentRepository.findByIdAndAdvertisementId(commentId, adsId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        Users currentUser = usersService.getCurrentUser();

        if (!isOwnerOrAdmin(comment, currentUser)) {
            throw new ForbiddenException("You don`t have delete comment");
        }

        commentRepository.deleteById(commentId);
    }

    /**
     * метод для обновления информации комментария
     * @param adsId - ID объявления
     * @param commentId - ID коммента, который нужно изменить
     * @param commentUpdate - DTO, необходимое для ответа
     * @return - возвращаем DTO со всей информацией коммента
     */

    public CommentDTO updateComment(Long adsId, Long commentId, CreateOrUpdateComment commentUpdate) {
        Comment comment = commentRepository.findByIdAndAdvertisementId(commentId, adsId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        Users currentUser = usersService.getCurrentUser();

        if (!isOwnerOrAdmin(comment, currentUser)) {
            throw new ForbiddenException("You don`t have edit comment");
        }

        comment.setText(commentUpdate.getText());
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toCommentDTO(updatedComment);
    }

    /**
     * доп метод ля проверки прав доступа
     *      - isOwner права авторизованного пользователя
     *      - isAdmin - права администратора
     */

    private boolean isOwnerOrAdmin(Comment comment, Users currentUser) {
        boolean isOwner = comment.getUser().getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);

        return isOwner || isAdmin;
    }
}
