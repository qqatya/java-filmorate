package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException(String message) {
        super("Жанр с id = " + message + " не найден");
    }
}
