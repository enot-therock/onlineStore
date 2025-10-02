package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.excepption.ForbiddenException;
import ru.skypro.homework.excepption.NotFoundException;
import ru.skypro.homework.excepption.UnauthorizedException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.dto.UpdateUser;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.repository.UsersRepository;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UsersService(UsersRepository usersRepository,
                        PasswordEncoder passwordEncoder,
                        UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * метод для смены пароля пользователя
     * @param currentPassword - текущий пароль
     * @param newPassword - новый пароль
     * @return - возвращаем логическое да/нет, как результат метода
     * passwordEncoder - необходим для работы с зашифрованным паролем.
     * С помощью него делаем две проверки:
     *      - проверяем совпадает ли текущий пароль с тем, на который мы меняем
     *      - проверяем что новый пароль не совпадает со старым
     */

    public boolean setPassword(String currentPassword, String newPassword) {
        Users user = getCurrentUser();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ForbiddenException("Current password is incorrect");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ForbiddenException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);

        return true;
    }

    /**
     * метод для обновления информации об авторизованном пользователе
     * @return - возвращает измененные данные
     */

    public UpdateUser updateUser(String firstName, String lastName, String phone) {
        Users currentUser = getCurrentUser();

        if (firstName != null) {
            currentUser.setFirstName(firstName);
        }

        if (lastName != null) {
            currentUser.setLastName(lastName);
        }

        if (phone != null) {
            currentUser.setPhone(phone);
        }

        Users user = usersRepository.save(currentUser);
        return userMapper.toUpdateUserDTO(user);
    }

    /**
     * метод для получения текущего пользователя
     * authentication - проверяем аутентификацию пользователя
     */

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }

        String username = authentication.getName();
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
