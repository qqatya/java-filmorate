package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final FilmValidator filmValidator;

    @Override
    public Film createFilm(Film film) {
        filmValidator.validate(film);
        return filmRepository.insertFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidator.validate(film);
        if (filmRepository.getAllFilms().stream().anyMatch(current -> Objects.equals(current.getId(), film.getId()))) {
            log.info("Updating film with id = {}", film.getId());
            return filmRepository.updateFilm(film);
        }
        throw new FilmNotFoundException(String.valueOf(film.getId()));
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Returning all films");
        return filmRepository.getAllFilms();
    }
}
