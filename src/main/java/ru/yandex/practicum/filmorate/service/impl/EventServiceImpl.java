package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.EventRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.EventService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Override
    public List<Event> getFeedByUserId(Integer id) {
        if (!userRepository.doesExist(id)) {
            throw new UserNotFoundException(String.valueOf(id));
        }
        log.info("Getting feed of userId = {}", id);
        return eventRepository.getEventsByUserId(id);
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.insertEvent(event);
    }
}
