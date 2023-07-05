package ru.yandex.practicum.filmorate.exception;

public class RatingNotFoundException  extends RuntimeException {

    public RatingNotFoundException(String message) {
        super("Рейтинг с id = " + message + " не найден");
    }
}
