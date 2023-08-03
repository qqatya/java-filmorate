package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.GenreRepositoryImpl;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreRepositoryTest {

    private final GenreRepositoryImpl genreRepository;

    private final FilmRepositoryImpl filmRepository;
    private final RatingService ratingService;

    @Test
    @Order(1)
    void insertFilmGenres() {
        Film film = filmRepository.insertFilm(Film.builder()
                .name("The Butterfly Effect")
                .description("The Butterfly Effect is a 2004 American science fiction thriller")
                .releaseDate(LocalDate.of(2004, 1, 23))
                .duration(113L)
                .mpa(ratingService.getRatingById(1))
                .build());
        Set<Genre> genres = Set.of(Genre.builder().id(2).name("Драма").build(),
                Genre.builder().id(4).name("Триллер").build());

        Set<Genre> insertedGenres = genreRepository.insertFilmGenres(genres, film.getId());

        assertEquals(2, insertedGenres.size());
        assertTrue(insertedGenres.stream().anyMatch(genre -> genre.getId() == 4));
    }

    @Test
    @Order(2)
    void getByFilmId() {
        Film film = filmRepository.insertFilm(Film.builder()
                .name("The Butterfly Effect")
                .description("The Butterfly Effect is a 2004 American science fiction thriller")
                .releaseDate(LocalDate.of(2004, 1, 23))
                .duration(113L)
                .genres(Set.of(Genre.builder().id(2).name("Драма").build(),
                        Genre.builder().id(4).name("Триллер").build()))
                        .mpa(ratingService.getRatingById(1))
                .build());
        Set<Genre> genres = genreRepository.getByFilmId(film.getId());

        assertEquals(2, genres.size());
        assertTrue(genres.stream().anyMatch(genre -> genre.getId() == 4));
    }

    @Test
    @Order(3)
    void updateFilmGenres() {
        Set<Genre> genres = Set.of(Genre.builder().id(2).name("Драма").build());

        Set<Genre> insertedGenres = genreRepository.updateFilmGenres(genres, 1);

        assertEquals(1, insertedGenres.size());
        assertTrue(insertedGenres.stream().noneMatch(genre -> genre.getId() == 4));
        assertTrue(insertedGenres.stream().anyMatch(genre -> genre.getId() == 2));
    }

    @Test
    @Order(4)
    void deleteFilmGenres() {
        genreRepository.deleteFilmGenres(1);

        assertEquals(0, genreRepository.getByFilmId(1).size());
    }

    @Test
    @Order(5)
    void getAllGenres() {
        Genre comedy = Genre.builder().id(1).name("Комедия").build();
        Genre fighting = Genre.builder().id(6).name("Боевик").build();

        List<Genre> genres = genreRepository.getAllGenres();

        assertTrue(genres.contains(comedy));
        assertTrue(genres.contains(fighting));
    }

    @Test
    @Order(6)
    void getGenreById() {
        Optional<Genre> genreOptional = genreRepository.getGenreById(3);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 3)
                );
    }
}