package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.repository.UsersRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private Users createTestUser(String username, String encodedPassword) {
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+1234567890");
        user.setRole(Role.USER);
        user.setEnabled(true);
        return user;
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
        String username = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        Users user = createTestUser(username, encodedPassword);

        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        boolean result = authService.login(username, password);

        assertTrue(result);
        verify(usersRepository).findByUsername(username);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    void login_WithInvalidPassword_ShouldReturnFalse() {
        String username = "test@example.com";
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword123";
        Users user = createTestUser(username, encodedPassword);

        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        boolean result = authService.login(username, password);

        assertFalse(result);
        verify(usersRepository).findByUsername(username);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    void register_WithNewUser_ShouldReturnTrue() {
        Register register = createTestRegister();
        String encodedPassword = "encodedPassword123";

        when(usersRepository.existsByUsername(register.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(register.getPassword())).thenReturn(encodedPassword);
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = authService.register(register);

        assertTrue(result);
        verify(usersRepository).existsByUsername(register.getUsername());
        verify(passwordEncoder).encode(register.getPassword());
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void register_WithExistingUsername_ShouldReturnFalse() {

        Register register = createTestRegister();

        when(usersRepository.existsByUsername(register.getUsername())).thenReturn(true);

        boolean result = authService.register(register);

        assertFalse(result);
        verify(usersRepository).existsByUsername(register.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    void login_WithNullPassword() {
        String username = "test@example.com";
        String encodedPassword = "encodedPassword123";
        Users user = createTestUser(username, encodedPassword);

        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(null, encodedPassword)).thenReturn(false);

        boolean result = authService.login(username, null);

        assertFalse(result);
        verify(passwordEncoder).matches(null, encodedPassword);
    }
}
