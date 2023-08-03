package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {
    private final GenreRepository genreRepository;
    private final RatingService ratingService;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .mpa(ratingService.getRatingById(rs.getInt("rating_id")))
                .genres(genreRepository.getByFilmId(rs.getInt("id")))
                .build();
    }
}
