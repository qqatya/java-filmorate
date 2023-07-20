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
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.RatingRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RatingRepositoryTest {

    private final RatingRepositoryImpl ratingRepository;

    private final FilmRepositoryImpl filmRepository;

    @Test
    @Order(1)
    void getByFilmId() {
        Film film = filmRepository.insertFilm(Film.builder()
                .name("The Butterfly Effect")
                .description("The Butterfly Effect is a 2004 American science fiction thriller")
                .releaseDate(LocalDate.of(2004, 1, 23))
                .duration(113L)
                .mpa(Rating.builder().id(3).name("PG-13").build())
                .build());

        Optional<Rating> ratingOptional = ratingRepository.getByFilmId(film.getId());

        assertThat(ratingOptional)
                .isPresent()
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("id", 3)
                );

    }

    @Test
    @Order(2)
    void getAllRatings() {
        Rating pg13 = Rating.builder().id(3).name("PG-13").build();
        Rating nc17 = Rating.builder().id(5).name("NC-17").build();

        List<Rating> ratings = ratingRepository.getAllRatings();

        assertTrue(ratings.contains(pg13));
        assertTrue(ratings.contains(nc17));
    }

    @Test
    @Order(3)
    void getRatingById() {
        Optional<Rating> ratingOptional = ratingRepository.getRatingById(1);

        assertThat(ratingOptional)
                .isPresent()
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("id", 1)
                );
    }
}