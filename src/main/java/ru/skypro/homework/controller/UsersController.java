package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;

import java.awt.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UsersController {

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/set_password")
    public NewPassword setPassword(@RequestBody String currentPassword, String newPassword) {
        return new NewPassword(currentPassword, newPassword);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public UserDTO getUsersInfo() {
        return new UserDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/me")
    public UpdateUser editUsersDate(@RequestBody String firstName, String lastName, String phone) {
        return new UpdateUser(firstName, lastName, phone);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/me/image")
    public Image editUsersImage(@RequestBody String image) {
        return null;
    }
}
