package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {

    @InjectMocks
    private UsersController usersController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    @Test
    void setPassword_ShouldReturnNewPassword() throws Exception {
        String currentPassword = "oldPass";
        String newPassword = "newPass";

        mockMvc.perform(post("/users/set_password")
                        .param("currentPassword", currentPassword)
                        .param("newPassword", newPassword))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.currentPassword").value(currentPassword))
                        .andExpect(jsonPath("$.newPassword").value(newPassword));
    }

    @Test
    void getUsersInfo_ReturnUsers() throws Exception{
        mockMvc.perform(get("/users/me")).andExpect(status().isOk());
    }

    @Test
    void editUsersDate_ReturnUser() throws Exception {
        mockMvc.perform(patch("/users/me")
                        .param("firstName", "Lev")
                        .param("lastName", "Tolstoy")
                        .param("phone", "+1234567890"))
                .andExpect(status().isOk());
    }

    @Test
    void editUsersImage_ReturnNull() throws Exception {
        mockMvc.perform(patch("/users/me")).andReturn();
    }
}
