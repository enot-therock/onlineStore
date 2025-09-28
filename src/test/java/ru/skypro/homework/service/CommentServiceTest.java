package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private UsersService usersService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentService commentService;

    private Users createTestUser(Long id, String username, Role role) {
        Users user = new Users();
        user.setId(id);
        user.setUsername(username);
        user.setRole(role);
        return user;
    }

    private Advertisement createTestAd(Long id) {
        Advertisement ad = new Advertisement();
        ad.setId(id);
        ad.setTitle("Test Ad");
        return ad;
    }

    private Comment createTestComment(Long id, Users user, Advertisement ad, String text) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setUser(user);
        comment.setAdvertisement(ad);
        comment.setText(text);
        comment.setCreatedAt(Instant.now());
        return comment;
    }

    @Test
    void createComment_ReturnCommentDTO() {
        Long adsId = 1L;
        Users user = createTestUser(1L, "test@example.com", Role.USER);
        Advertisement ad = createTestAd(adsId);
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText("Test comment");
        commentDTO.setCreatedAt(System.currentTimeMillis());

        Comment comment = createTestComment(1L, user, ad, "Test comment");
        CommentDTO expectedCommentDTO = new CommentDTO();
        expectedCommentDTO.setText("Test comment");

        when(usersService.getCurrentUser()).thenReturn(user);
        when(advertisementRepository.findById(adsId)).thenReturn(Optional.of(ad));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.commentToEntity(comment)).thenReturn(expectedCommentDTO);

        CommentDTO result = commentService.createComment(adsId, commentDTO);

        assertNotNull(result);
        assertEquals("Test comment", result.getText());

        verify(usersService).getCurrentUser();
        verify(advertisementRepository).findById(adsId);
        verify(commentRepository).save(any(Comment.class));
        verify(commentMapper).commentToEntity(comment);
    }

    @Test
    void createComment_ThrowNotFoundException() {
        Long adsId = 999L;
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText("Test comment");

        when(usersService.getCurrentUser()).thenReturn(createTestUser(1L, "test@example.com", Role.USER));
        when(advertisementRepository.findById(adsId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.createComment(adsId, commentDTO));

        verify(advertisementRepository).findById(adsId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void getAllComments_ShouldReturnComments() {
        Long adsId = 1L;
        Users user = createTestUser(1L, "test@example.com", Role.USER);
        Advertisement ad = createTestAd(adsId);

        Comment comment1 = createTestComment(1L, user, ad, "Comment 1");
        Comment comment2 = createTestComment(2L, user, ad, "Comment 2");
        List<Comment> comments = List.of(comment1, comment2);

        CommentDTO commentDTO1 = new CommentDTO();
        commentDTO1.setText("Comment 1");
        CommentDTO commentDTO2 = new CommentDTO();
        commentDTO2.setText("Comment 2");

        when(advertisementRepository.existsById(adsId)).thenReturn(true);
        when(commentRepository.findByAdvertisementIdOrderByCreatedAtDesc(adsId)).thenReturn(comments);
        when(commentMapper.commentToEntity(comment1)).thenReturn(commentDTO1);
        when(commentMapper.commentToEntity(comment2)).thenReturn(commentDTO2);

        Comments result = commentService.getAllComments(adsId);

        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResult().size());
        assertEquals("Comment 1", result.getResult().get(0).getText());

        verify(advertisementRepository).existsById(adsId);
        verify(commentRepository).findByAdvertisementIdOrderByCreatedAtDesc(adsId);
        verify(commentMapper, times(2)).commentToEntity(any(Comment.class));
    }

    @Test
    void deleteComment_ShouldDeleteComment() {
        Long adsId = 1L;
        Long commentId = 1L;
        Users owner = createTestUser(1L, "owner@example.com", Role.USER);
        Advertisement ad = createTestAd(adsId);
        Comment comment = createTestComment(commentId, owner, ad, "Test comment");

        when(commentRepository.findByIdAndAdvertisementId(commentId, adsId)).thenReturn(Optional.of(comment));
        when(usersService.getCurrentUser()).thenReturn(owner);

        commentService.deleteComment(adsId, commentId);

        verify(commentRepository).findByIdAndAdvertisementId(commentId, adsId);
        verify(usersService).getCurrentUser();
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void updateComment_ShouldUpdateComment() {
        Long adsId = 1L;
        Long commentId = 1L;
        Users owner = createTestUser(1L, "owner@example.com", Role.USER);
        Advertisement ad = createTestAd(adsId);
        Comment comment = createTestComment(commentId, owner, ad, "Old text");

        CreateOrUpdateComment updateRequest = new CreateOrUpdateComment();
        updateRequest.setText("Updated text");

        Comment updatedComment = createTestComment(commentId, owner, ad, "Updated text");
        CommentDTO expectedCommentDTO = new CommentDTO();
        expectedCommentDTO.setText("Updated text");

        when(commentRepository.findByIdAndAdvertisementId(commentId, adsId)).thenReturn(Optional.of(comment));
        when(usersService.getCurrentUser()).thenReturn(owner);
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);
        when(commentMapper.commentToEntity(updatedComment)).thenReturn(expectedCommentDTO);

        CommentDTO result = commentService.updateComment(adsId, commentId, updateRequest);

        assertNotNull(result);
        assertEquals("Updated text", result.getText());

        verify(commentRepository).save(comment);
        assertEquals("Updated text", comment.getText());
    }
}
