package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.eventenum.Entity;
import ru.yandex.practicum.filmorate.model.eventenum.Operation;

import javax.swing.tree.RowMapper;
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
                .operation(Operation.valueOf(rs.getString("operation")))
                .eventType(Entity.valueOf(rs.getString("eventType")))
                .build();
    }
}