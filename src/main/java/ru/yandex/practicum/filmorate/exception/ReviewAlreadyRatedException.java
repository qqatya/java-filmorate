package ru.yandex.practicum.filmorate.exception;

public class ReviewAlreadyRatedException extends RuntimeException {
    public ReviewAlreadyRatedException(String message) {
        super("Такая оценка рейтинга с id = " + message + " уже существует");
    }
}
