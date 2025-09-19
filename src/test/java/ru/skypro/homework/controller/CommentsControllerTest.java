package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentsControllerTest {

    @InjectMocks
    private CommentsController commentsController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentsController).build();
    }

    @Test
    void getAdComments_ReturnComment() throws Exception {
        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk());
    }

    @Test
    void createComment_ReturnComment() throws Exception {
        String commentJson = "{\"text\": \"\"}";

        mockMvc.perform(post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                        .andExpect(status().isOk());
    }

    @Test
    void deleteComment_ReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/1/comments/5"))
                .andExpect(status().isOk());
    }

    @Test
    void editComment_ReturnNewComment() throws Exception {
        String updateJson = "{\"text\": \"\"}";

        mockMvc.perform(patch("/ads/1/comments/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                        .andExpect(status().isOk());
    }
}
