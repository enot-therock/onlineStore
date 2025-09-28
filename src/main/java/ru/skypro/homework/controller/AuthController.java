package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.model.dto.Login;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * эндпоинт для авторизации пользователя
     * возможные коды ответа:
     *      - 200 Ок (успешное выполнение)
     *      - 401 Unauthorized (не авторизован)
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        log.info("Login for user: {}", login.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getUsername(),
                            login.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok().build();
        }
        catch (AuthenticationException e) {
            log.warn("Failed login for user: {}", login.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * эндпоинт для регистрации пользователя
     * возможные коды ответа:
     *      - 201 Created (зарегестрирован / создан)
     *      - 400 Bad Request (невалидные данные)
     */

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        log.info("Register user: {}", register.getUsername());
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.warn("Failed register for user: {}", register.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
