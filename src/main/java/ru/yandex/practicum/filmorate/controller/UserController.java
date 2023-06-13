package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Создание пользователя
     *
     * @param user Объект, содержащий данные для создания
     * @return Созданный пользователь
     */
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Обновление пользователя
     *
     * @param user Объект, содержащий данные для обновления
     * @return Обновленный пользователь
     */
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * Получение всех пользователей
     *
     * @return Список пользователей
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
