package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final FilmValidator filmValidator;

    @Override
    public Film createFilm(Film film) {
        filmValidator.validate(film);
        log.info("Creating film with id = {}", film.getId());
        return filmRepository.insertFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidator.validate(film);
        log.info("Updating film with id = {}", film.getId());
        return filmRepository.insertFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Returning all films");
        return filmRepository.getAllFilms();
    }
}
