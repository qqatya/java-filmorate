package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.EventRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.EventService;

import java.util.List;

import static ru.yandex.practicum.filmorate.model.type.ExceptionType.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Override
    public List<Event> getFeedByUserId(Integer id) {
        if (!userRepository.doesExist(id)) {
            throw new NotFoundException(USER_NOT_FOUND.getValue() + id);
        }
        log.info("Getting feed of userId = {}", id);
        return eventRepository.getEventsByUserId(id);
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.insertEvent(event);
    }
}
