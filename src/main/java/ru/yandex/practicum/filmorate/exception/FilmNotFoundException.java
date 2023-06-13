package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String message) {
        super("Фильм с id = " + message + " не найден");
    }
}
