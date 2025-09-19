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
import org.springframework.web.bind.annotation.RequestParam;
import ru.skypro.homework.model.dto.AdvertisementDTO;
import ru.skypro.homework.model.dto.ExtendedAd;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdvertisementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AdvertisementController advertisementController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(advertisementController).build();
    }

    @Test
    void getAllAds_ReturnOk() throws Exception {
        mockMvc.perform(get("/ads")).andExpect(status().isOk());
    }

    @Test
    void createdNewAds_ReturnOk() throws Exception {
        mockMvc.perform(post("/ads")
                        .param("author", "1")
                        .param("image", "image")
                        .param("pk", "2")
                        .param("price", "3")
                        .param("title", "title"))
                .andExpect(status().isOk());
    }

    @Test
    void getAdsId_ReturnOk() throws Exception {
        mockMvc.perform(get("/ads/1", 1)).andExpect(status().isOk());
    }

    @Test
    void deleteAdsId_ReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/1")).andExpect(status().isOk());
    }

    @Test
    void aditAds_ReturnOk() throws Exception {
        String createOrUpdateAd_Json = "{" +
                "\"title\": \"title\"," +
                " \"price\": \"1\"," +
                " \"description\": \"description\"}";

        mockMvc.perform(post("/ads/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createOrUpdateAd_Json))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsersAds_ReturnOk() throws Exception {
        mockMvc.perform(get("/ads/me")).andExpect(status().isOk());
    }

    @Test
    void getImage_ReturnOk() throws Exception {
        mockMvc.perform(patch("/ads/1/image")).andExpect(status().isOk());
    }
}
