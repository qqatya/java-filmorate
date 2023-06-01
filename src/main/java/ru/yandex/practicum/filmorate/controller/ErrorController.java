package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorInfo;

@Slf4j
@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    /**
     * Обработчик ValidationException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo processValidationException(ValidationException e) {
        log.debug("Encountered a validation error: {}", e.getMessage());
        return new ErrorInfo(e.getMessage());
    }

    /**
     * Обработчик UserNotFoundException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo processUserNotFoundException(UserNotFoundException e) {
        log.debug("Can not find user: {}", e.getMessage());
        return new ErrorInfo(e.getMessage());
    }

    /**
     * Обработчик непредвиденных ошибок
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfo processException(RuntimeException e) {
        log.error("Unexpected error: ", e);
        return new ErrorInfo(e.getMessage());
    }
}
