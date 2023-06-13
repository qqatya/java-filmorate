package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidatorTest {
    final FilmValidator filmValidator = new FilmValidator();

    @Test
    void doesntThrowValidationExceptionWhenDataIsValid() {
        Film fIlm = Film.builder()
                .id(1)
                .name("Джедайские практики")
                .description("Очень джедайские")
                .releaseDate(LocalDate.of(2005, 9, 10))
                .duration(140L)
                .build();

        assertDoesNotThrow(() -> filmValidator.validate(fIlm));
    }

    @Test
    void throwsValidationExceptionExceptionExceptionWhenNameIsEmpty() {
        Film fIlm = Film.builder()
                .id(1)
                .name("")
                .description("Очень джедайские")
                .releaseDate(LocalDate.of(2005, 9, 10))
                .duration(140L)
                .build();

        assertThrows(ValidationException.class, () -> filmValidator.validate(fIlm),
                "Название не может быть пустым");
    }

    @Test
    void throwsValidationExceptionExceptionExceptionWhenDescriptionContains201Symbols() {
        Film fIlm = Film.builder()
                .id(1)
                .name("Джедайские практики")
                .description("Очень джедайские джедайские джедайские джедайские джедайские джедайские джедайские "
                        + "джедайские джедайские джедайские джедайские джедайские джедайские джедайские джедайские "
                        + "джедайские джедайские джедайск")
                .releaseDate(LocalDate.of(2005, 9, 10))
                .duration(140L)
                .build();

        assertThrows(ValidationException.class, () -> filmValidator.validate(fIlm),
                "Длина описания не может превышать 200 символов");
    }

    @Test
    void throwsValidationExceptionExceptionExceptionWhenReleaseDateIsBefore28_12_1895() {
        Film fIlm = Film.builder()
                .id(1)
                .name("Джедайские практики")
                .description("Очень джедайские")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(140L)
                .build();

        assertThrows(ValidationException.class, () -> filmValidator.validate(fIlm),
                "Дата релиза не может быть раньше 28 декабря 1895 года");
    }

    @Test
    void throwsValidationExceptionExceptionExceptionWhenDurationIsNegative() {
        Film fIlm = Film.builder()
                .id(1)
                .name("Джедайские практики")
                .description("Очень джедайские")
                .releaseDate(LocalDate.of(1985, 12, 27))
                .duration(-1L)
                .build();

        assertThrows(ValidationException.class, () -> filmValidator.validate(fIlm),
                "Дата релиза не может быть раньше 28 декабря 1985 года");
    }

}