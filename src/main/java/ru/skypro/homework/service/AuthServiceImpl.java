package ru.skypro.homework.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.model.entity.Users;
import ru.skypro.homework.repository.UsersRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final UsersRepository usersRepository;

    public AuthServiceImpl(PasswordEncoder encoder,
                           UsersRepository usersRepository) {
        this.encoder = encoder;
        this.usersRepository = usersRepository;
    }

    /**
     * метод авторизации пользователя
     * @param username - email
     * @param password - пароль
     * @return - получаем результат авториции, как логическое выражение
     */

    @Override
    public boolean login(String username, String password) {
        return usersRepository.findByUsername(username)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    /**
     * метод регистрации нового пользователя
     * @param register - DTO с исходными данными
     * @return - возвращаем логическое выражение как результат регистрации
     * и сохраняем нового пользователя
     */

    @Override
    public boolean register(Register register) {
        if (usersRepository.existsByUsername(register.getUsername())) {
            return false;
        }

        Users newUser = new Users();
        newUser.setUsername(register.getUsername());
        newUser.setPassword(encoder.encode(register.getPassword()));
        newUser.setFirstName(register.getFirstName());
        newUser.setLastName(register.getLastName());
        newUser.setPhone(register.getPhone());
        newUser.setRole(register.getRole());
        newUser.setEnabled(true);

        usersRepository.save(newUser);

        return true;
    }
}
