package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.model.dto.CommentDTO;
import ru.skypro.homework.model.dto.Comments;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentsControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentsController commentController;

    @Test
    void getAdComments_ShouldReturnComments() {
        Long adsId = 1L;
        CommentDTO commentDTO1 = new CommentDTO();
        commentDTO1.setText("Comment 1");
        CommentDTO commentDTO2 = new CommentDTO();
        commentDTO2.setText("Comment 2");
        List<CommentDTO> commentDTOs = List.of(commentDTO1, commentDTO2);
        Comments comments = new Comments(2, commentDTOs);

        when(commentService.getAllComments(adsId)).thenReturn(comments);

        ResponseEntity<Comments> response = commentController.getAdComments(adsId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCount());
        assertEquals(2, response.getBody().getResult().size());

        verify(commentService).getAllComments(adsId);
    }

    @Test
    void createComment_ShouldReturnCreatedComment() {
        Long adsId = 1L;
        CommentDTO requestDTO = new CommentDTO();
        requestDTO.setText("New comment");

        CommentDTO responseDTO = new CommentDTO();
        responseDTO.setText("New comment");

        when(commentService.createComment(adsId, requestDTO)).thenReturn(responseDTO);

        ResponseEntity<CommentDTO> response = commentController.createComment(adsId, requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New comment", response.getBody().getText());

        verify(commentService).createComment(adsId, requestDTO);
    }

    @Test
    void deleteAds_ShouldReturnOk() {
        Long adsId = 1L;
        Long commentId = 1L;

        doNothing().when(commentService).deleteComment(adsId, commentId);

        ResponseEntity<Void> response = commentController.deleteAds(adsId, commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentService).deleteComment(adsId, commentId);
    }

    @Test
    void editComment_ShouldReturnUpdatedComment() {
        Long adsId = 1L;
        Long commentId = 1L;
        CreateOrUpdateComment updateRequest = new CreateOrUpdateComment();
        updateRequest.setText("Updated comment");

        CommentDTO responseDTO = new CommentDTO();
        responseDTO.setText("Updated comment");

        when(commentService.updateComment(adsId, commentId, updateRequest)).thenReturn(responseDTO);

        ResponseEntity<CommentDTO> response = commentController.editComment(adsId, commentId, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated comment", response.getBody().getText());

        verify(commentService).updateComment(adsId, commentId, updateRequest);
    }
}
