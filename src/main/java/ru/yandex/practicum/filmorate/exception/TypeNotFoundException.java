package ru.yandex.practicum.filmorate.exception;

public class TypeNotFoundException extends RuntimeException {
    public TypeNotFoundException(String message) {
        super("Тип запроса со значением: " + message + " не найден");
    }
}