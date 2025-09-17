package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentsController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ads/{id}/comments")
    public Comments getAdComments(@PathVariable int id) {
        return new Comments();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/ads/{id}/comments")
    public Comments createComment(@RequestBody CreateOrUpdateComment createOrUpdateComment) {
        return new Comments();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/ads/{adId}/comments/{commentId}")
    public void deleteAds(@PathVariable int adId, int commentId) {
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/ads/{adId}/comments/{commentId}")
    public Comments editComment(@PathVariable int adId, int commentId) {
        return new Comments();
    }
}
