package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final EventService eventService;

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

    /**
     * Получение пользователя по идентификатору
     *
     * @param id Идентификатор пользователя
     * @return Пользователь
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    /**
     * Добавление в друзья
     *
     * @param userId   Идентификатор пользователя
     * @param friendId Идентификатор добавляемого друга
     * @return Пользователь с обновленным списком друзей
     */
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer userId,
                          @PathVariable Integer friendId) {
        return userService.addFriend(userId, friendId);
    }

    /**
     * Удаление из друзей
     *
     * @param userId   Идентификатор пользователя
     * @param friendId Идентификатор удаляемого друга
     * @return Пользователь с обновленным списком друзей
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Integer userId,
                             @PathVariable Integer friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    /**
     * Получение списка друзей пользователя
     *
     * @param id Идентификатор пользователя
     * @return Список друзей
     */
    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        return userService.getAllFriends(id);
    }

    /**
     * Получение списка общих друзей двух пользователей
     *
     * @param userId  Идентификатор пользователя
     * @param otherId Идентификатор пользователя
     * @return Список общих друзей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer userId,
                                       @PathVariable Integer otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    /**
     * Удаление пользователя по идентификатору
     *
     * @param id Идентификатор пользователя
     */
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
    }

    /**
     * Поиск рекомендаций по идентификатору пользователя
     *
     * @param id Идентификатор пользователя
     * @return Список рекомендованных фильмов
     */
    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable Integer id) {
        return userService.getRecommendations(id);
    }

    /**
     * Получение ленты событий по идентификатору пользователя
     *
     * @param id Идентификатор пользователя
     * @return Список событий
     */
    @GetMapping("{id}/feed")
    public List<Event> getFeedByUserId(@PathVariable Integer id) {
        return eventService.getFeedByUserId(id);
    }
}