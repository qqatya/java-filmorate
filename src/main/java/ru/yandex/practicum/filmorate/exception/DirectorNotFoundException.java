package ru.yandex.practicum.filmorate.exception;

public class DirectorNotFoundException extends RuntimeException {
    public DirectorNotFoundException(String message) {
        super("Режиссер с id = " + message + " не найден");
    }
}
