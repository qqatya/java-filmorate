package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EventNotFoundException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.EventRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class EventRepositoryImpl implements EventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final EventMapper eventMapper;

    private static final String SQL_INSERT_EVENT = "INSERT INTO public.event "
            + "(operation, timestamp, type, person_id, entity_id) "
            + "VALUES(:operation, :timestamp, :type, :person_id, :entity_id)";

    private static final String SQL_GET_EVENT_BY_ID = "SELECT id, timestamp, operation, type, person_id, entity_id "
            + "FROM public.event WHERE id = :id";

    private static final String SQL_GET_EVENTS_BY_USER_ID = "SELECT id, timestamp, operation, type, person_id, "
            + "entity_id FROM public.event WHERE person_id = :id";

    @Override
    public Event insertEvent(Event event) {
        MapSqlParameterSource params = getParams(event);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(SQL_INSERT_EVENT, params, holder);
        Integer eventId = holder.getKey().intValue();

        return getEventById(eventId).orElseThrow(() -> new EventNotFoundException(String.valueOf(eventId)));
    }

    @Override
    public List<Event> getEventsByUserId(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(SQL_GET_EVENTS_BY_USER_ID, params, eventMapper);
    }

    private Optional<Event> getEventById(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(SQL_GET_EVENT_BY_ID, params, eventMapper).stream().findFirst();


    }

    private MapSqlParameterSource getParams(Event event) {
        var params = new MapSqlParameterSource();
        params.addValue("timestamp", event.getTimestamp());
        params.addValue("person_id", event.getUserId());
        params.addValue("type", event.getEventType().toString());
        params.addValue("operation", event.getOperation().toString());
        params.addValue("entity_id", event.getEntityId());
        return params;
    }
}