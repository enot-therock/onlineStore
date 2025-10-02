package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skypro.homework.model.dto.Login;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.service.AuthServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private Login createTestLogin() {
        Login login = new Login();
        login.setUsername("test@example.com");
        login.setPassword("password123");
        return login;
    }

    private Register createTestRegister() {
        Register register = new Register();
        register.setUsername("test@example.com");
        register.setPassword("password123");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+1234567890");
        register.setRole(Role.USER);
        return register;
    }

    @Test
    void login_WithValidCredentials() {
        Login login = createTestLogin();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        ResponseEntity<?> response = authController.login(login);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() {
        Login login = createTestLogin();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<?> response = authController.login(login);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void register_WithNewUser() {
        Register register = createTestRegister();

        when(authService.register(register)).thenReturn(true);

        ResponseEntity<?> response = authController.register(register);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(authService).register(register);
    }

    @Test
    void register_WithExistingUsername() {
        Register register = createTestRegister();

        when(authService.register(register)).thenReturn(false);

        ResponseEntity<?> response = authController.register(register);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(authService).register(register);
    }
}
