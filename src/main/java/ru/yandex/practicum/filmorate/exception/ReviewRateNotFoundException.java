package ru.yandex.practicum.filmorate.exception;

public class ReviewRateNotFoundException extends RuntimeException {
    public ReviewRateNotFoundException(String message) {
        super("Оценка для отзыва с id = " + message + " не найдена");
    }
}
