package ru.yandex.practicum.filmorate.exception;

public class SearchTypeNotFoundException extends RuntimeException {
    public SearchTypeNotFoundException(String message) {
        super("Тип запроса со значением: " + message + " не найден");
    }
}
