package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(OrderAnnotation.class)
class FilmRepositoryTest {

    private final FilmRepositoryImpl filmRepository;
    private final RatingService ratingService;

    private final UserRepositoryImpl userRepository;

    private static final String DESCRIPTION = "Shutter Island is a 2010 American neo-noir psychological thriller film "
            + "directed by Martin Scorsese";

    private static final Long DURATION = 139L;
    private static final LocalDate RELEASE_DATE = LocalDate.of(2010, 2, 19);

    @Test
    @Order(1)
    void insertFilm() {
        String name = "Shutter Island";

        Film film = filmRepository.insertFilm(Film.builder()
                .id(1)
                .name(name)
                .description(DESCRIPTION)
                .releaseDate(RELEASE_DATE)
                .duration(DURATION)
                .mpa(ratingService.getRatingById(1))
                .build());

        assertTrue(film.getId() == 1 || film.getId() == 2);
        assertEquals(name, film.getName());
        assertEquals(DESCRIPTION, film.getDescription());
        assertEquals(DURATION, film.getDuration());
        assertEquals(RELEASE_DATE, film.getReleaseDate());
    }

    @Test
    @Order(2)
    void updateFilm() {
        String name = "Shutter Island UPD";

        Film film = filmRepository.updateFilm(Film.builder()
                .id(1)
                .name(name)
                .description(DESCRIPTION)
                .releaseDate(RELEASE_DATE)
                .duration(DURATION)
                .mpa(ratingService.getRatingById(1))
                .build());

        assertEquals(1, film.getId());
        assertEquals(name, film.getName());
        assertEquals(DESCRIPTION, film.getDescription());
        assertEquals(DURATION, film.getDuration());
        assertEquals(RELEASE_DATE, film.getReleaseDate());
    }

    @Test
    @Order(3)
    void getAllFilms() {
        List<Film> films = filmRepository.getAllFilms();

        assertNotNull(films);
    }

    @Test
    @Order(4)
    void getFilmById() {
        Optional<Film> filmOptional = filmRepository.getFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @Order(5)
    void putLike() {
        User user = userRepository.insertUser(User.builder()
                .email("ivan@ya.ru")
                .login("iwwwwan")
                .name("Ivan Ivanov")
                .birthday(LocalDate.of(2000, 9, 10))
                .build());

        Film film = filmRepository.putLike(1, user.getId());

        assertTrue(film.getUsersLiked().contains(user.getId()));

    }

    @Test
    @Order(6)
    void getPopularFilms() {
        String name = "Shutter Island";

        Film film = filmRepository.insertFilm(Film.builder()
                .id(1)
                .name(name)
                .description(DESCRIPTION)
                .releaseDate(RELEASE_DATE)
                .duration(DURATION)
                .mpa(ratingService.getRatingById(1))
                .build());

        List<Film> films = filmRepository.getPopularFilms(1, null, 2010);

        assertEquals(1, films.size());
    }

    @Test
    @Order(7)
    void deleteLike() {
        Film film = filmRepository.deleteLike(1, 1);

        assertEquals(0, film.getUsersLiked().size());
    }

    @Test
    @Order(8)
    void doesExist() {
        assertTrue(filmRepository.doesExist(1));
    }
}