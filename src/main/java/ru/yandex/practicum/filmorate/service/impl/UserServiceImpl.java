package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.UserValidator;

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
        return userRepository.insertUser(user);
    }

    @Override
    public User updateUser(User user) {
        userValidator.validate(user);
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

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.valueOf(id));
        }
        return userOptional.get();
    }
}
