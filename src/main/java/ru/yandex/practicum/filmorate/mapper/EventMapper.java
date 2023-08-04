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
                .eventId(rs.getInt("eventId"))
                .timestamp(rs.getLong("timestamp"))
                .entityId(rs.getInt("entityId"))
                .userId(rs.getInt("userId"))
                .operationType(OperationType.valueOf(rs.getString("operation")))
                .eventType(EventType.valueOf(rs.getString("eventType")))
                .build();
    }
}