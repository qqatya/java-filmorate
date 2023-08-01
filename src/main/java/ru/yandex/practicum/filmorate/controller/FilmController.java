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

    /**
     * Получение фильма по идентификатору
     *
     * @param id Идентификатор фильма
     * @return Фильм
     */
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    /**
     * Добавление лайка пользователя
     *
     * @param id     Идентификатор фильма
     * @param userId Идентификатор пользователя
     * @return Фильм с обновленным списком лайков
     */
    @PutMapping("{id}/like/{userId}")
    public Film putLike(@PathVariable Integer id,
                        @PathVariable Integer userId) {
        return filmService.putLike(id, userId);
    }

    /**
     * Удаление лайка пользователя
     *
     * @param id     Идентификатор фильма
     * @param userId Идентификатор пользователя
     * @return Фильм с обновленным списком лайков
     */
    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id,
                           @PathVariable Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    /**
     * Получение фильмов по маскимальному количеству лайков
     *
     * @param count Количество фильмов
     * @return Список фильмов
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    /**
     * Вывод общих с другом фильмов с сортировкой по их популярности
     *
     * @param userId   Идентификатор пользователя, запрашивающего информацию
     * @param friendId Идентификатор пользователя, с которым необходимо сравнить список фильмов.
     * @return Список фильмов, отсортированных по популярности
     */
    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam Integer userId,@RequestParam Integer friendId) {
        return filmService.getCommonFilms(userId,friendId);
    }
}
