package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UpdateUser;
import ru.skypro.homework.model.dto.UserDTO;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.service.UsersService;

import java.awt.*;

@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UsersController {

    private final UsersService usersService;
    private final UserMapper userMapper;

    public UsersController(UsersService usersService,
                           UserMapper userMapper) {
        this.usersService = usersService;
        this.userMapper = userMapper;
    }

    /**
     * эндпоинт для смены пароля
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     *      - 403 Forbidden (недостаточно прав доступа)
     */

    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword) {
        usersService.setPassword(
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * эндпоинт для получения информации об авторизованном пользователе
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     */

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUsersInfo() {
        Users currentUser = usersService.getCurrentUser();
            return ResponseEntity.ok(userMapper.userToEntity(currentUser));
    }

    /**
     * эндпоинт для обновления информации об авторизованном пользователе
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     */

    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUserData(@RequestBody UpdateUser updateUser) {
        UpdateUser userDTO = usersService.updateUser(
                updateUser.getFirstName(),
                updateUser.getLastName(),
                updateUser.getPhone()
        );
        return ResponseEntity.ok(userDTO);
    }

    /**
     * эндпоинт для обновления аватара авторизованного пользователя
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     */

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/me/image")
    public Image editUsersImage(@RequestBody String image) {
        return null;
    }
}
