package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.EventRepository;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.model.type.ExceptionType.EVENT_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Repository
public class EventRepositoryImpl implements EventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final EventMapper eventMapper;

    @Override
    public Event insertEvent(Event event) {
        String sqlInsertEvent = "INSERT INTO public.event "
                + "(operation, timestamp, type, person_id, entity_id) "
                + "VALUES(:operation, :timestamp, :type, :person_id, :entity_id)";
        MapSqlParameterSource params = getParams(event);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(sqlInsertEvent, params, holder);
        Integer eventId = holder.getKey().intValue();

        return getEventById(eventId).orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND.getValue() + eventId));
    }

    @Override
    public List<Event> getEventsByUserId(Integer id) {
        String sqlGetEventsByUserId = "SELECT id, timestamp, operation, type, person_id, "
                + "entity_id FROM public.event WHERE person_id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(sqlGetEventsByUserId, params, eventMapper);
    }

    private Optional<Event> getEventById(Integer id) {
        String sqlGetEventById = "SELECT id, timestamp, operation, type, person_id, entity_id "
                + "FROM public.event WHERE id = :id";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(sqlGetEventById, params, eventMapper).stream().findFirst();


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