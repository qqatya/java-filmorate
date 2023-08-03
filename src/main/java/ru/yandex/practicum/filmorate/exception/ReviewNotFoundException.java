package ru.yandex.practicum.filmorate.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super("Отзыв с id = " + message + " не найден");
    }
}
