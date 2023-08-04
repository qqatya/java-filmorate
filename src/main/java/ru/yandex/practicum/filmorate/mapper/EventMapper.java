package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .timestamp(rs.getLong("timestamp"))
                .userId(rs.getInt("person_id"))
                .eventType(EventType.valueOf(rs.getString("type")))
                .operation(Operation.valueOf(rs.getString("operation")))
                .eventId(rs.getInt("id"))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}