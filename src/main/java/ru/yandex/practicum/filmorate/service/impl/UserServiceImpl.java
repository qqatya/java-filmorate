package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public User createUser(User user) {
        userValidator.validate(user);
        log.info("Creating user with id = {}", user.getId());
        return userRepository.insertUser(user);
    }

    @Override
    public User updateUser(User user) {
        userValidator.validate(user);
        log.info("Updating user with id = {}", user.getId());
        return userRepository.insertUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Returning all users");
        return userRepository.getAllUsers();
    }
}
