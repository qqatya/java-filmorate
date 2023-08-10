package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(OrderAnnotation.class)
class FilmRepositoryTest {

    private final FilmRepositoryImpl filmRepository;
    private final RatingService ratingService;
    private final GenreService genreService;
    private final DirectorService directorService;
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

        Film film = filmRepository.putLike(1, user.getId(), 6);

        assertTrue(film.getUsersLiked().contains(user.getId()));

    }

    @Test
    @Order(6)
    void getPopularFilms() {
        Set<Genre> genres = new HashSet<>();
        genres.add(genreService.getGenreById(1));
        Set<Director> directors = new HashSet<>();
        Director director = directorService.createDirector(Director.builder()
                .id(1)
                .name("Wars")
                .build());
        directors.add(director);
        Set<Director> directors2 = new HashSet<>();
        Director director2 = directorService.createDirector(Director.builder()
                .id(2)
                .name("Dir")
                .build());
        directors2.add(director2);
        Film film = filmRepository.insertFilm(Film.builder()
                .name("Star Wars")
                .description("DescriptionTest1")
                .releaseDate(LocalDate.of(2010, 9, 10))
                .duration(20L)
                .mpa(ratingService.getRatingById(1))
                .genres(genres)
                .directors(directors2)
                .build());
        Film film2 = filmRepository.insertFilm(Film.builder()
                .name("A star is born")
                .description("DescriptionTest2")
                .releaseDate(LocalDate.of(2010, 9, 10))
                .duration(20L)
                .mpa(ratingService.getRatingById(1))
                .genres(genres)
                .directors(directors)
                .build());
        Film film3 = filmRepository.insertFilm(Film.builder()
                .name("Scream")
                .description("DescriptionTest3")
                .releaseDate(LocalDate.of(2010, 9, 10))
                .duration(20L)
                .mpa(ratingService.getRatingById(1))
                .genres(genres)
                .directors(directors)
                .build());
        User user = userRepository.insertUser(User.builder()
                .email("common@email.com")
                .login("TestLogin")
                .name("Name1")
                .birthday(LocalDate.of(2000, 9, 10))
                .build());
        User user2 = userRepository.insertUser(User.builder()
                .email("com@email.com")
                .login("TestLogin2")
                .name("Name2")
                .birthday(LocalDate.of(2000, 9, 10))
                .build());
        User user3 = userRepository.insertUser(User.builder()
                .email("con@email.com")
                .login("TestLogin3")
                .name("Name3")
                .birthday(LocalDate.of(2000, 9, 10))
                .build());
        filmRepository.putLike(film.getId(), user.getId(), 6);
        filmRepository.putLike(film.getId(), user2.getId(), 6);
        filmRepository.putLike(film2.getId(), user2.getId(), 7);
        filmRepository.putLike(film2.getId(), user3.getId(), 7);
        filmRepository.putLike(film3.getId(), user2.getId(), 1);

        List<Film> films = filmRepository.getPopularFilms(3, 1, 2010);
        assertEquals(films.size(), 3);
        Film topFilm = films.get(0);
        assertEquals(topFilm.getId(), film2.getId());

        List<Film> filmsByDirector = filmRepository.getFilmsByDirectorId(1, "likes");
        assertEquals(filmsByDirector.size(), 2);
        Film topFilmByDirector = filmsByDirector.get(0);
        assertEquals(topFilmByDirector.getId(), film2.getId());

        List<Film> byTitle = filmRepository.searchFilms("Star", "title");
        assertEquals(byTitle.size(), 2);
        Film popularBySearch = byTitle.get(0);
        assertEquals(popularBySearch.getId(), film2.getId());

        List<Film> byDirector = filmRepository.searchFilms("wArS", "director");
        assertEquals(byDirector.size(), 2);
        Film notPopularBySearch = byDirector.get(1);
        assertEquals(notPopularBySearch.getId(), film3.getId());

        List<Film> byAll = filmRepository.searchFilms("wArs", "title,director");
        assertEquals(3, byAll.size());
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