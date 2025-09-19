package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.dto.Comments;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentsController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/comments")
    public Comments getAdComments(@PathVariable int id) {
        return new Comments();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/comments")
    public Comments createComment(@RequestBody CreateOrUpdateComment createOrUpdateComment) {
        return new Comments();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteAds(@PathVariable int adId,
                          @PathVariable int commentId) {
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{adId}/comments/{commentId}")
    public Comments editComment(@PathVariable int adId,
                                @PathVariable int commentId) {
        return new Comments();
    }
}
