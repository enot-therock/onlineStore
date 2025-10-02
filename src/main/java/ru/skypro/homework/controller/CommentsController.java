package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.dto.CommentDTO;
import ru.skypro.homework.model.dto.Comments;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentsController {

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * эндпоинт для получения комментариев объявления
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     *      - 404 Not Found (нет информации)
     */

    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getAdComments(@PathVariable("id") Long adsId) {
        Comments comments = commentService.getAllComments(adsId);
        return ResponseEntity.ok(comments);
    }

    /**
     * эндпоинт для добавления комментария к объявлению
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     *      - 404 Not Found (нет информации)
     */

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable("id") Long adsId,
                                                    @RequestBody CommentDTO commentDTO) {

        CommentDTO comments = commentService.createComment(adsId, commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(comments);
    }

    /**
     * эндпоинт для удаления комментария
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     *      - 403 Forbidden (недостаточно прав доступа)
     *      - 404 Not Found (нет информации)
     */

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteAds(@PathVariable Long adId, @PathVariable Long commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * эндпоинт для обновления комментария
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     *      - 403 Forbidden (недостаточно прав доступа)
     *      - 404 Not Found (нет информации)
     */

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> editComment(@PathVariable Long adId,
                                                  @PathVariable Long commentId,
                                                  @RequestBody CreateOrUpdateComment updateComment) {
        CommentDTO commentDTO = commentService.updateComment(adId, commentId, updateComment);
        return ResponseEntity.ok(commentDTO);
    }
}
