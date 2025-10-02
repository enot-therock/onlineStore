package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.dto.NewPassword;
import ru.skypro.homework.model.dto.UpdateUser;
import ru.skypro.homework.model.dto.UserDTO;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UsersController {

    private final UsersService usersService;
    private final UserMapper userMapper;
    private final ImageService imageService;

    public UsersController(UsersService usersService,
                           UserMapper userMapper,
                           ImageService imageService) {
        this.usersService = usersService;
        this.userMapper = userMapper;
        this.imageService = imageService;
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
            return ResponseEntity.ok(userMapper.toUserDTO(currentUser));
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

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> editUsersImage(@RequestParam("image") MultipartFile imageFile) throws IOException {
        Users currentUser = usersService.getCurrentUser();

        String image = imageService.savedUserImage(currentUser, imageFile);
        return ResponseEntity.ok("Image update successfully: " + image);
    }

    @GetMapping(value = "/me/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getCurrentUserImage() throws IOException {
        Users currentUser = usersService.getCurrentUser();
        if (currentUser.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] imageData = imageService.getImageData(currentUser.getImage().getId());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(currentUser.getImage().getMediaType()))
                .body(imageData);
    }
}
