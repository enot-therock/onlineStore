package ru.skypro.homework.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.excepption.ForbiddenException;
import ru.skypro.homework.excepption.NotFoundException;
import ru.skypro.homework.excepption.UnauthorizedException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.dto.UpdateUser;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.repository.UsersRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    Authentication authentication;

    @InjectMocks
    private UsersService usersService;

    private Users testUser;

    /**
     * создаем пользователя до начала тестов
     * encodedOldPassword - закодированный пароль
     */

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setId(1L);
        testUser.setUsername("test@mail.com");
        testUser.setPassword("encodedOldPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPhone("+79991234567");
        testUser.setEnabled(true);
    }

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    /**
     * настройка аутентификации для тестов
     * lenient() - использует нужные заглушки для каждого теста
     */

    private void setUpAuthenticatedUser(String username) {
        lenient().when(authentication.isAuthenticated()).thenReturn(true);
        lenient().when(authentication.getName()).thenReturn(username);
        lenient().when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void setPassword_WhenValidData() {
        setUpAuthenticatedUser("test@mail.com");

        when(usersRepository.findByUsername("test@mail.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldPassword123", "encodedOldPassword"))
                .thenReturn(true);
        when(passwordEncoder.matches("newPassword456", "encodedOldPassword"))
                .thenReturn(false);
        when(passwordEncoder.encode("newPassword456")).thenReturn("encodedOldPassword");
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        boolean result = usersService.setPassword("oldPassword123", "newPassword456");

        assertThat(result).isTrue();
        verify(passwordEncoder).matches("oldPassword123", "encodedOldPassword");
        verify(passwordEncoder).matches("newPassword456", "encodedOldPassword");
        verify(passwordEncoder).encode("newPassword456");
        verify(usersRepository).save(testUser);
        assertThat(testUser.getPassword()).isEqualTo("encodedOldPassword");
    }

    /**
     * never() - проверяем что метод никогда не вызывался
     */

    @Test
    void setPassword_whenCurrentPasswordInCorrect() {
        setUpAuthenticatedUser("test@mail.com");

        when(usersRepository.findByUsername("test@mail.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "encodedOldPassword"))
                .thenReturn(false);

        assertThatThrownBy(() -> usersService.setPassword("wrongPassword", "newPassword456"))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Current password is incorrect");

        verify(passwordEncoder, never()).encode(anyString());
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    void setPassword_WhenUserNotAuthenticated() {
        assertThatThrownBy(() -> usersService.setPassword("oldPassword123", "newPassword456"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("User not authenticated");
    }

    @Test
    void setPassword_WhenUserNotFound() {
        setUpAuthenticatedUser("nonexistent@mail.com");

        when(usersRepository.findByUsername("nonexistent@mail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usersService.setPassword("oldPassword123", "newPassword456"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("User not authenticated");
    }

    @Test
    void updateUser_WhenAllFieldsProvided() {
        setUpAuthenticatedUser("test@mail.com");

        when(usersRepository.findByUsername("test@mail.com")).thenReturn(Optional.of(testUser));
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        UpdateUser expectedUpdateUser = new UpdateUser(
                "NewFirstName", "NewLastName", "+79992222222");
        when(userMapper.toUpdateUserDTO(testUser)).thenReturn(expectedUpdateUser);

        UpdateUser result = usersService.updateUser(
                "NewFirstName", "NewLastName", "+79992222222");

        assertThat(result).isEqualTo(expectedUpdateUser);
        assertThat(testUser.getFirstName()).isEqualTo("NewFirstName");
        assertThat(testUser.getLastName()).isEqualTo("NewLastName");
        assertThat(testUser.getPhone()).isEqualTo("+79992222222");
        verify(usersRepository).save(testUser);
        verify(userMapper).toUpdateUserDTO(testUser);
    }

    @Test
    void updateUser_WhenOnlyFirstNameProvided() {
        setUpAuthenticatedUser("test@mail.com");

        when(usersRepository.findByUsername("test@mail.com")).thenReturn(Optional.of(testUser));
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);

        UpdateUser expectedUpdateUser = new UpdateUser("NewFirstName", "Doe", "+79991234567");
        when(userMapper.toUpdateUserDTO(testUser)).thenReturn(expectedUpdateUser);

        UpdateUser result = usersService.updateUser("NewFirstName", null, null);

        assertThat(result.getFirstName()).isEqualTo("NewFirstName");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getPhone()).isEqualTo("+79991234567");

        assertThat(testUser.getFirstName()).isEqualTo("NewFirstName");
        assertThat(testUser.getLastName()).isEqualTo("Doe");
        assertThat(testUser.getPhone()).isEqualTo("+79991234567");

        verify(usersRepository).save(testUser);
    }

    @Test
    void updateUser_WhenUserNotAuthenticated() {
        assertThatThrownBy(() -> usersService.updateUser(
                "NewFirstName", "NewLastName", "+79992222222"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("User not authenticated");
    }

    @Test
    void getCurrentUser_WhenUserAuthenticatedAndExists() {
        setUpAuthenticatedUser("test@mail.com");

        when(usersRepository.findByUsername("test@mail.com")).thenReturn(Optional.of(testUser));

        Users result = usersService.getCurrentUser();

        assertThat(result).isEqualTo(testUser);
        verify(usersRepository).findByUsername("test@mail.com");
    }

    @Test
    void getCurrentUser_WhenUserNotFoundInDatabase() {
        setUpAuthenticatedUser("test@mail.com");

        when(usersRepository.findByUsername("test@mail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usersService.getCurrentUser())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void getCurrentUser_WhenNoAuthentication() {
        assertThatThrownBy(() -> usersService.getCurrentUser())
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("User not authenticated");
    }
}
