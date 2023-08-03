package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.eventenum.Entity;
import ru.yandex.practicum.filmorate.model.eventenum.Operation;
import ru.yandex.practicum.filmorate.repository.EventRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
@Repository
public class EventRepositoryImpl implements EventRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void addEvent(Event event) {
        log.trace("Level: Repository. Class EventRepositoryImpl. Call of addEvent method");
        MapSqlParameterSource values = new MapSqlParameterSource();
        values.addValue("event_timestamp", Timestamp.from(Instant.ofEpochMilli(event.getTimestamp())));
        values.addValue("event_type", event.getEventType());
        values.addValue("operation", event.getOperation());
        values.addValue("entity_id", event.getEntityId());
        values.addValue("user_id", event.getUserId());

        String insertSql = "INSERT INTO events_log (event_timestamp, event_type, operation, entity_id, user_id) "
                + "VALUES (:event_timestamp, :event_type, :operation, :entity_id, :user_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(insertSql, values, keyHolder);
            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            if (id > 0) {
                event.setEventId(id);
            } else {
                throw new ValidationException("Событие в журнал не добавлено. Ошибка валидации записи");
            }
        } catch (DataAccessException e) {
            throw new ValidationException("Не удалось добавить событие в журнал. Причина: " + e.getMessage());
        }
    }

    @Override
    public List<Event> getUsersFeed(int userId) {
        log.trace("Layer: Repository. Class EventRepositoryImpl. Call of getUserFeed");
        String sql = "SELECT * FROM events_log WHERE user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        return Collections.singletonList(jdbcTemplate.query(sql, params, this::makeEvent));
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        return new Event(
                rs.getInt("id"),
                rs.getTimestamp("event_timestamp").toInstant().toEpochMilli(),
                Operation.valueOf(rs.getString("operation")),
                Entity.valueOf(rs.getString("event_type")),
                rs.getInt("entity_id"),
                rs.getInt("user_id")
        );
    }
}