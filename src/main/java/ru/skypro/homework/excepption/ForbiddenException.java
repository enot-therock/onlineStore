package ru.skypro.homework.excepption;

public class ForbiddenException extends RuntimeException {

    /**
     * ошибка, которая возникает, если недостаточно или отсутствуют права доступа
     * (код ошибки 403) - Forbidden
     */

    public ForbiddenException(String message) {
        super(message);
    }
}
