package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;
import ru.yandex.practicum.filmorate.repository.UserLikeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {

    private final GenreRepository genreRepository;

    private final RatingRepository ratingRepository;

    private final DirectorRepository directorRepository;

    private final UserLikeRepository userLikeRepository;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer filmId = rs.getInt("id");

        return Film.builder()
                .id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .usersLiked(userLikeRepository.getUsersLikedByFilmId(filmId))
                .mpa(ratingRepository.getByFilmId(filmId).orElse(null))
                .genres(genreRepository.getByFilmId(filmId))
                .directors(directorRepository.getByFilmId(filmId))
                .build();
    }
}
