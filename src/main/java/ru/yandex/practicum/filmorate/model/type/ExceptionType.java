package ru.yandex.practicum.filmorate.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionType {
    DIRECTOR_NOT_FOUND("Не найден режиссер с id = "),
    USER_NOT_FOUND("Не найден пользователь с id = "),
    EVENT_NOT_FOUND("Не найдено событие с id = "),
    FILM_NOT_FOUND("Не найден фильм с id = "),
    GENRE_NOT_FOUND("Не найден жанр с id = "),
    RATING_NOT_FOUND("Не найден рейтинг с id = "),
    REVIEW_NOT_FOUND("Не найден отзыв с id = "),
    REVIEW_RATE_NOT_FOUND("Не найдена оценка для отзыва с id = "),
    SEARCH_TYPE_NOT_FOUND("Не найден тип запроса со значением = ");

    private final String value;

}
