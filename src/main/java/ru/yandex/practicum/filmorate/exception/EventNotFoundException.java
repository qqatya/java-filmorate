package ru.yandex.practicum.filmorate.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super("Событие с id = " + message + " не найдено");
    }
}
