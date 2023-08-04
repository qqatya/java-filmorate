package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.Operation;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final DirectorRepository directorRepository;
    private final FilmValidator filmValidator;
    private final EventService eventService;

    @Override
    public Film createFilm(Film film) {
        filmValidator.validate(film);
        return filmRepository.insertFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidator.validate(film);
        if (filmRepository.doesExist(film.getId())) {
            log.info("Updating filmId = {}", film.getId());
            return filmRepository.updateFilm(film);
        }
        throw new FilmNotFoundException(String.valueOf(film.getId()));
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Returning all films");
        return filmRepository.getAllFilms();
    }

    @Override
    public Film getFilmById(Integer id) {
        log.info("Getting filmId = {}", id);
        return filmRepository.getFilmById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.valueOf(id)));
    }

    @Override
    public Film putLike(Integer id, Integer userId) {
        if (!filmRepository.doesExist(id)) {
            throw new FilmNotFoundException(String.valueOf(id));
        }
        if (!userRepository.doesExist(userId)) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        log.info("Adding like from userId = {} to filmId = {}", userId, id);
        Film updatedFilm = filmRepository.putLike(id, userId);
        Event event = eventService.addEvent(new Event(userId, EventType.LIKE, Operation.ADD, id));

        log.info("Created eventId = {}", event.getEventId());
        return updatedFilm;
    }

    @Override
    public Film deleteLike(Integer id, Integer userId) {
        if (!filmRepository.doesExist(id)) {
            throw new FilmNotFoundException(String.valueOf(id));
        }
        if (!userRepository.doesExist(userId)) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        log.info("Removing like from userId = {} to filmId = {}", userId, id);
        Film updatedFilm = filmRepository.deleteLike(id, userId);
        Event event = eventService.addEvent(new Event(userId, EventType.LIKE, Operation.REMOVE, id));

        log.info("Created eventId = {}", event.getEventId());
        return updatedFilm;

    }

    @Override
    public List<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        log.info("Getting popular films amount = {}, genreId = {}, year = {}", count, genreId, year);
        return filmRepository.getPopularFilms(count, genreId, year);
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        if (!userRepository.doesExist(userId)) {
            throw new UserNotFoundException(userId.toString());
        }
        if (!userRepository.doesExist(friendId)) {
            throw new UserNotFoundException(friendId.toString());
        }
        log.info("Getting common films userId = {} and friendId = {}", userId, friendId);
        return filmRepository.getCommonFilms(userId, friendId);
    }

    @Override
    public void deleteFilmById(Integer id) {
        if (!filmRepository.doesExist(id)) {
            throw new FilmNotFoundException(String.valueOf(id));
        }
        log.info("Deleting filmId = {}", id);
        filmRepository.deleteFilmById(id);
    }

    @Override
    public List<Film> getFilmsByDirectorIdSorted(Integer directorId, String sortBy) {
        if (!directorRepository.doesExist(directorId)) {
            throw new DirectorNotFoundException(String.valueOf(directorId));
        }
        log.info("Getting films with directorId = {} by sort = {}", directorId, sortBy);
        return filmRepository.getFilmsByDirectorId(directorId, sortBy);
    }

    @Override
    public List<Film> searchFilms(String query, String by) {
        log.info("Getting search films, where query = {} and by = {}", query, by);
        return filmRepository.searchFilms(query, by);
    }
}