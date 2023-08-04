package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.filmorate.exception.*;
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
     * Обработчик FilmNotFoundException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(FilmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo processFilmNotFoundException(FilmNotFoundException e) {
        log.debug("Can not find film: {}", e.getMessage());
        return new ErrorInfo(e.getMessage());
    }

    /**
     * Обработчик RatingNotFoundException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(RatingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo processRatingNotFoundException(RatingNotFoundException e) {
        log.debug("Can not find rating: {}", e.getMessage());
        return new ErrorInfo(e.getMessage());
    }

    /**
     * Обработчик GenreNotFoundException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(GenreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo processGenreNotFoundException(GenreNotFoundException e) {
        log.debug("Can not find genre: {}", e.getMessage());
        return new ErrorInfo(e.getMessage());
    }

    /**
     * Обработчик DirectorNotFoundException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(DirectorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo processDirectorNotFoundException(DirectorNotFoundException e) {
        log.debug("Can not find director: {}", e.getMessage());
        return new ErrorInfo(e.getMessage());
    }

    /**
     * Обработчик ReviewNotFoundException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo processReviewNotFoundException(ReviewNotFoundException e) {
        log.debug("Can not find review: {}", e.getMessage());
        return new ErrorInfo(e.getMessage());
    }

    /**
     * Обработчик ReviewAlreadyRatedException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(ReviewAlreadyRatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo processReviewAlreadyRatedException(ReviewAlreadyRatedException e) {
        log.debug("Review rate already exists: {}", e.getMessage());
        return new ErrorInfo(e.getMessage());
    }

    /**
     * Обработчик EventNotFoundException
     *
     * @param e Эксепшн
     * @return Объект, содержащий сообщение об ошибке
     */
    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo processEventNotFoundException(EventNotFoundException e) {
        log.debug("Can not find event: {}", e.getMessage());
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
