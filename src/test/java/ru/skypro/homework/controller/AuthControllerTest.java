package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.service.AuthServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthServiceImpl authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void login_True_ReturnOk() throws Exception {
        when(authService.login("tester", "password")).thenReturn(true);

        String login = "{\"username\": \"tester\", \"password\": \"password\"}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                        .andExpect(status().isOk());
    }

    @Test
    void login_Fals_ReturnUnauthorized() throws Exception {
        when(authService.login("tester", "password")).thenReturn(false);

        String loginJson = "{\"username\": \"tester\", \"password\": \"password\"}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                        .andExpect(status().isUnauthorized());
    }

    @Test
    void register_TrueData_ReturnCreated() throws Exception {
        when(authService.register(any(Register.class))).thenReturn(true);

        String registerJson = "{" +
                "\"username\": \"newuser\"," +
                "\"password\": \"password123\"," +
                "\"firstName\": \"John\"," +
                "\"lastName\": \"Doe\"," +
                "\"phone\": \"+1234567890\"," +
                "\"role\": \"USER\"" +
                "}";

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isCreated());
    }

    @Test
    void register_FalseData_ReturnBadRequest() throws Exception {
        when(authService.register(any(Register.class))).thenReturn(false);

        String registerJson = "{" +
                "\"username\": \"newuser\"," +
                "\"password\": \"password123\"," +
                "\"firstName\": \"John\"," +
                "\"lastName\": \"Doe\"," +
                "\"phone\": \"+1234567890\"," +
                "\"role\": \"USER\"" +
                "}";

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isBadRequest());
    }
}
