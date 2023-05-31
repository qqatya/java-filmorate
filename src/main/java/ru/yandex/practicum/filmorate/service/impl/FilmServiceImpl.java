package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;

    @Override
    public Film createFilm(Film film) {
        return filmRepository.insertFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmRepository.insertFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }
}
