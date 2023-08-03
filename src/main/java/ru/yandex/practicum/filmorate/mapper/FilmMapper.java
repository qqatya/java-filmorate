package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {

    private final GenreRepository genreRepository;

    private final RatingRepository ratingRepository;

    private final DirectorRepository directorRepository;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer filmId = rs.getInt("id");

        return Film.builder()
                .id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .mpa(ratingRepository.getByFilmId(filmId).orElse(null))
                .genres((genreRepository.getByFilmId(rs.getInt("id"))))
                .directors(directorRepository.getByFilmId(filmId))
                .build();
    }
}
