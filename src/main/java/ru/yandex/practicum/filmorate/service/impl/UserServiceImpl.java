package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public User createUser(User user) {
        userValidator.validate(user);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        return userRepository.insertUser(user);
    }

    @Override
    public User updateUser(User user) {
        userValidator.validate(user);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (userRepository.getAllUsers().stream().anyMatch(current -> Objects.equals(current.getId(), user.getId()))) {
            log.info("Updating user with id = {}", user.getId());
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
        Optional<User> userOptional = userRepository.getUserById(id);

        log.info("Getting user by id = {}", id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.valueOf(id));
        }
        return userOptional.get();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        if (userRepository.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        if (userRepository.getUserById(friendId).isEmpty()) {
            throw new UserNotFoundException(String.valueOf(friendId));
        }
        log.info("Adding userId = {} to friends of userId = {}", friendId, userId);
        return userRepository.addFriend(userId, friendId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        Optional<User> userOptional = userRepository.getUserById(userId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        if (userRepository.getUserById(friendId).isEmpty()) {
            throw new UserNotFoundException(String.valueOf(friendId));
        }
        if (userOptional.get().getFriends().contains(friendId)) {
            log.info("Deleting userId = {} from friends of userId = {}", friendId, userId);
            return userRepository.deleteFriend(userId, friendId);
        }

        throw new ValidationException(
                String.format("Пользователь с id = %d не состоит в друзьях у пользователя с id = %d", friendId, userId));
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        if (userRepository.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(String.valueOf(id));
        }
        log.info("Getting all friends of userId = {}", id);
        return userRepository.getAllFriends(id);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        if (userRepository.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException(String.valueOf(userId));
        }
        if (userRepository.getUserById(otherId).isEmpty()) {
            throw new UserNotFoundException(String.valueOf(otherId));
        }
        log.info("Getting common friends of userId = {} and userId = {}", userId, otherId);
        return userRepository.getCommonFriends(userId, otherId);
    }
}
