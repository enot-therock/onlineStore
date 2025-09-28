package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UpdateUser;
import ru.skypro.homework.model.dto.UserDTO;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.service.UsersService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UsersService usersService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UsersController userController;

    @Test
    void setPassword_WithValidData_ShouldReturnOk() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("currentPassword");
        newPassword.setNewPassword("newPassword");

        when(usersService.setPassword("currentPassword", "newPassword"))
                .thenReturn(true);

        ResponseEntity<?> response = userController.setPassword(newPassword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usersService).setPassword("currentPassword", "newPassword");
    }

    @Test
    void getUsersInfo_ShouldReturnUserDTO() {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("test@example.com");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setEmail("test@example.com");

        when(usersService.getCurrentUser()).thenReturn(user);
        when(userMapper.userToEntity(user)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUsersInfo();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("test@example.com", response.getBody().getEmail());

        verify(usersService).getCurrentUser();
        verify(userMapper).userToEntity(user);
    }

    @Test
    void updateUserData_WithValidData_ShouldReturnUpdatedUser() {
        UpdateUser updateUserRequest = new UpdateUser();
        updateUserRequest.setFirstName("Jane");
        updateUserRequest.setLastName("Smith");
        updateUserRequest.setPhone("+0987654321");

        UpdateUser updateUserResponse = new UpdateUser("Jane", "Smith", "+0987654321");

        when(usersService.updateUser("Jane", "Smith", "+0987654321"))
                .thenReturn(updateUserResponse);

        ResponseEntity<UpdateUser> response = userController.updateUserData(updateUserRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jane", response.getBody().getFirstName());
        assertEquals("Smith", response.getBody().getLastName());
        assertEquals("+0987654321", response.getBody().getPhone());

        verify(usersService).updateUser("Jane", "Smith", "+0987654321");
    }
}
