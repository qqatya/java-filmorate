package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.OperationType;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("id"))
                .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
                .entityId(rs.getInt("entity_id"))
                .userId(rs.getInt("person_id"))
                .operationType(OperationType.valueOf(rs.getString("operation")))
                .eventType(EventType.valueOf(rs.getString("type")))
                .build();
    }
}