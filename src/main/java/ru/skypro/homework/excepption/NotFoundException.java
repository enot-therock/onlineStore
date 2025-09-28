package ru.skypro.homework.excepption;

public class NotFoundException extends RuntimeException {

    /**
     * ошибка, которая возникает, если недостаточно данных (пустые поля) или они не найдены
     * (код ошибки 404) - Not found
     */

    public NotFoundException(String message) {
        super(message);
    }
}
