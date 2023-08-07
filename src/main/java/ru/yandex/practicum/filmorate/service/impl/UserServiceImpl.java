package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.type.EventType;
import ru.yandex.practicum.filmorate.model.type.Operation;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final EventService eventService;

    @Override
    public User createUser(User user) {
        userValidator.validate(user);
        return userRepository.insertUser(user);
    }

    @Override
    public User updateUser(User user) {
        userValidator.validate(user);
        if (userRepository.doesExist(user.getId())) {
            log.info("Updating userId = {}", user.getId());
            return userRepository.updateUser(user);
        }
        throw new UserNotFoundException(String.valueOf(user.getId()));
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Returning all users");
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(Integer id) {
        log.info("Getting userId = {}", id);
        return userRepository.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        if (!userRepository.doesExist(userId)) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        if (!userRepository.doesExist(friendId)) {
            throw new UserNotFoundException(String.valueOf(friendId));
        }
        log.info("Adding userId = {} to friends of userId = {}", friendId, userId);
        User updatedUser = userRepository.addFriend(userId, friendId);
        Event event = eventService.addEvent(new Event(userId, EventType.FRIEND, Operation.ADD, friendId));

        log.info("Created eventId = {}", event.getEventId());
        return updatedUser;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        Optional<User> userOptional = userRepository.getUserById(userId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        if (!userRepository.doesExist(friendId)) {
            throw new UserNotFoundException(String.valueOf(friendId));
        }
        Set<Integer> friendIds = userOptional.get().getFriends().stream()
                .map(Friend::getId)
                .collect(Collectors.toSet());

        if (friendIds.contains(friendId)) {
            log.info("Deleting userId = {} from friends of userId = {}", friendId, userId);
            User updatedUser = userRepository.deleteFriend(userId, friendId);
            Event event = eventService.addEvent(new Event(userId, EventType.FRIEND, Operation.REMOVE, friendId));

            log.info("Created eventId = {}", event.getEventId());
            return updatedUser;
        }

        throw new ValidationException(
                String.format("Пользователь с id = %d не состоит в друзьях у пользователя с id = %d", friendId, userId));
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        if (!userRepository.doesExist(id)) {
            throw new UserNotFoundException(String.valueOf(id));
        }
        log.info("Getting all friends of userId = {}", id);
        return userRepository.getAllFriends(id);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        if (!userRepository.doesExist(userId)) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        if (!userRepository.doesExist(otherId)) {
            throw new UserNotFoundException(String.valueOf(otherId));
        }
        log.info("Getting common friends of userId = {} and userId = {}", userId, otherId);
        return userRepository.getCommonFriends(userId, otherId);
    }

    @Override
    public void deleteUserById(Integer id) {
        if (!userRepository.doesExist(id)) {
            throw new UserNotFoundException(String.valueOf(id));
        }
        log.info("Deleting userId = {}", id);
        userRepository.deleteUserById(id);
    }

    public List<Film> getRecommendations(Integer id) {
        if (!userRepository.doesExist(id)) {
            throw new UserNotFoundException(String.valueOf(id));
        }
        log.info("Getting recommendation films by userId = {}", id);
        return userRepository.getRecommendations(id);
    }
}
