package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventController {
    private EventController userService;

    @GetMapping("/{id}/feed")
    public List<Event> getUserFeed(@PathVariable Integer id) {
        return userService.getUserFeed(id);
    }
}
