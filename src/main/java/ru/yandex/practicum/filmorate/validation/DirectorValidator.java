package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

@Component
@Slf4j
public class DirectorValidator {
    /**
     * Валидация входящего объекта режиссера
     *
     * @param director Объект, содержащие данные о режиссере
     */
    public void validate(Director director) {
        if (director.getName() == null || director.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        log.debug("Director is valid");
    }
}