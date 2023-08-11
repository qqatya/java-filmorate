package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testing FilmRepository class")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(OrderAnnotation.class)
class FilmRepositoryTest {

    private final FilmRepositoryImpl filmRepository;
    private final RatingService ratingService;

    @Test
    @DisplayName("Testing insert film")
    void insertFilm() {
        Film film = filmRepository.insertFilm(Film.builder()
                .name("Joker")
                .description("d".repeat(100))
                .releaseDate(LocalDate.of(2019, 10, 9))
                .duration(122L)
                .mpa(ratingService.getRatingById(5))
                .build());

        assertNotNull(film.getId());
        assertEquals("Joker", film.getName());
        assertEquals("d".repeat(100), film.getDescription());
        assertEquals(122L, film.getDuration());
        assertEquals(LocalDate.of(2019, 10, 9), film.getReleaseDate());
    }

    @Test
    @DisplayName("Testing update film")
    void updateFilm() {
        Film film = filmRepository.updateFilm(Film.builder()
                .id(1)
                .name("Leon")
                .description("About killer")
                .releaseDate(LocalDate.of(1984, 12, 14))
                .duration(120L)
                .mpa(ratingService.getRatingById(1))
                .build());

        assertEquals(1, film.getId());
        assertEquals("Leon", film.getName());
        assertEquals("About killer", film.getDescription());
        assertEquals(120L, film.getDuration());
        assertEquals(LocalDate.of(1984, 12, 14), film.getReleaseDate());
    }

    @Test
    @DisplayName("Testing get all films")
    void getAllFilms() {
        List<Film> films = filmRepository.getAllFilms();

        assertNotNull(films);
    }

    @Test
    @DisplayName("Testing get film by id")
    void getFilmById() {
        Optional<Film> filmOptional = filmRepository.getFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @DisplayName("Testing put like to film")
    void putLike() {
        Film film = filmRepository.putLike(6, 6, 6);

        assertTrue(film.getUsersLiked().contains(6));

    }

    @Test
    @DisplayName("Testing get popular films")
    void getPopularFilms() {
        List<Film> films = filmRepository.getPopularFilms(2, 2, 2023);
        Film topFilm = films.get(0);

        assertNotNull(films);
        assertEquals(2, films.size());
        assertEquals(5, topFilm.getId());
    }

    @Test
    @DisplayName("Testing search")
    void search() {
        List<Film> byTitle = filmRepository.searchFilms("fRoZen", "title");
        Film filmT = byTitle.get(0);

        assertEquals(1, byTitle.size());
        assertEquals(4, filmT.getId());

        List<Film> byDirector = filmRepository.searchFilms("TOM", "director");
        Film filmD = byDirector.get(0);
        assertEquals(1, byDirector.size());
        assertEquals(1, filmD.getId());

        List<Film> byTitleDirector = filmRepository.searchFilms("lAnd", "title,director");
        Film filmTD = byTitleDirector.get(0);

        assertEquals(2, byTitleDirector.size());
        assertEquals(2, filmTD.getId());

        List<Film> byDirectorTitle = filmRepository.searchFilms("LeOn", "director,title");
        Film filmDT = byDirectorTitle.get(0);

        assertEquals(2, byDirectorTitle.size());
        assertEquals(5, filmDT.getId());
    }

    @Test
    @DisplayName("Testing delete like")
    void deleteLike() {
        Film film = filmRepository.deleteLike(3, 5);

        assertEquals(0, film.getUsersLiked().size());
    }

    @Test
    @DisplayName("Testing film exist")
    void doesExist() {
        assertTrue(filmRepository.doesExist(1));
    }
}