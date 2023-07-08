package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = genreRepository.getAllGenres();

        log.info("Found {} genres", genres.size());
        return genres;
    }

    @Override
    public Genre getGenreById(Integer id) {
        return genreRepository.getGenreById(id).orElseThrow(() -> new GenreNotFoundException(String.valueOf(id)));
    }
}
