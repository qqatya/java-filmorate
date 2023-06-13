package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    /**
     * Создание фильма
     *
     * @param film Объект, содержащий данные для создания
     * @return Созданный фильм
     */
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    /**
     * Обновление фильма
     *
     * @param film Объект, содержащий данные для обновления
     * @return Обновленный фильм
     */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    /**
     * Получение всех фильмов
     *
     * @return Список фильмов
     */
    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }
}
