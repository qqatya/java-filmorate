package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    final UserValidator userValidator = new UserValidator();

    @Test
    void doesntThrowValidationExceptionWhenDataIsValid() {
        User user = User.builder()
                .id(1)
                .email("hello@yandex.ru")
                .login("cyberlord3000")
                .name("Киберлорд Иванович")
                .birthday(LocalDate.of(2000, 9, 1))
                .build();

        assertDoesNotThrow(() -> userValidator.validate(user));
    }

    @Test
    void throwsValidationExceptionExceptionExceptionWhenEmailIsEmpty() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("cyberlord3000")
                .name("Киберлорд Иванович")
                .birthday(LocalDate.of(2000, 9, 1))
                .build();

        assertThrows(ValidationException.class, () -> userValidator.validate(user),
                "Электронная почта не может быть пустой и должна содержать символ @");
    }

    @Test
    void throwsValidationExceptionExceptionExceptionWhenEmailDoesntContainAtSign() {
        User user = User.builder()
                .id(1)
                .email("helloyandex.ru")
                .login("cyberlord3000")
                .name("Киберлорд Иванович")
                .birthday(LocalDate.of(2000, 9, 1))
                .build();

        assertThrows(ValidationException.class, () -> userValidator.validate(user),
                "Электронная почта не может быть пустой и должна содержать символ @");
    }

    @Test
    void throwsValidationExceptionExceptionExceptionWhenLoginIsEmpty() {
        User user = User.builder()
                .id(1)
                .email("hello@yandex.ru")
                .login("")
                .name("Киберлорд Иванович")
                .birthday(LocalDate.of(2000, 9, 1))
                .build();

        assertThrows(ValidationException.class, () -> userValidator.validate(user),
                "Логин не может быть пустым и содержать пробелы");
    }

    @Test
    void throwsValidationExceptionExceptionExceptionWhenLoginContainsSpaces() {
        User user = User.builder()
                .id(1)
                .email("hello@yandex.ru")
                .login("cyber lord3000")
                .name("Киберлорд Иванович")
                .birthday(LocalDate.of(2000, 9, 1))
                .build();

        assertThrows(ValidationException.class, () -> userValidator.validate(user),
                "Логин не может быть пустым и содержать пробелы");
    }

    @Test
    void setsLoginAsNameWhenNameIsEmpty() {
        User user = User.builder()
                .id(1)
                .email("hello@yandex.ru")
                .login("cyberlord3000")
                .name("")
                .birthday(LocalDate.of(2000, 9, 1))
                .build();

        userValidator.validate(user);
        assertEquals("cyberlord3000", user.getName());
    }

    @Test
    void throwsValidationExceptionExceptionExceptionWhenBirthdayIsAfterCurrentDate() {
        User user = User.builder()
                .id(1)
                .email("hello@yandex.ru")
                .login("cyber lord3000")
                .name("Киберлорд Иванович")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        assertThrows(ValidationException.class, () -> userValidator.validate(user),
                "Дата рождения не может быть в будущем времени");
    }

}