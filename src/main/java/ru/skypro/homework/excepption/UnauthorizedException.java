package ru.skypro.homework.excepption;

public class UnauthorizedException extends RuntimeException {

    /**
     * ошибка, которая возникает, если пользователь не авторизован
     * (код ошибки 401) - Unauthorized
     */

    public UnauthorizedException(String message) {
        super(message);
    }
}
