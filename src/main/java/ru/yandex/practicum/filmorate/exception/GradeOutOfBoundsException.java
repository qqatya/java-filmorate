package ru.yandex.practicum.filmorate.exception;

public class GradeOutOfBoundsException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Значение оценки должно быть в промежутке от 1 до 10 включительно";

    public GradeOutOfBoundsException() {
        super(ERROR_MESSAGE);
    }
}
