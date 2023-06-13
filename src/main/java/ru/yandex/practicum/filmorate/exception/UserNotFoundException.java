package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super("Пользователь с id = " + message + " не найден");
    }
}
