package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    /**
     * Создание пользователя
     *
     * @param user Объект, содержащий данные для создания
     * @return Созданный пользователь
     */
    User createUser(User user);

    /**
     * Обновление пользователя
     *
     * @param user Объект, содержащий данные для обновления
     * @return Обновленный пользователь
     */
    User updateUser(User user);

    /**
     * Получение всех пользователей
     *
     * @return Список пользователей
     */
    List<User> getAllUsers();

    /**
     * Получение пользователя по идентификатору
     *
     * @param id Идентификатор пользователя
     * @return Пользователь
     */
    User getUserById(Integer id);

    /**
     * Добавление в друзья
     *
     * @param userId   Идентификатор пользователя
     * @param friendId Идентификатор добавляемого друга
     * @return Пользователь с обновленным списком друзей
     */
    User addFriend(Integer userId, Integer friendId);

    /**
     * Удаление из друзей
     *
     * @param userId   Идентификатор пользователя
     * @param friendId Идентификатор удаляемого друга
     * @return Пользователь с обновленным списком друзей
     */
    User deleteFriend(Integer userId, Integer friendId);

    /**
     * Получение списка друзей пользователя
     *
     * @param id Идентификатор пользователя
     * @return Список друзей
     */
    List<User> getAllFriends(Integer id);

    /**
     * Получение списка общих друзей двух пользователей
     *
     * @param userId  Идентификатор пользователя
     * @param otherId Идентификатор пользователя
     * @return Список общих друзей
     */
    List<User> getCommonFriends(Integer userId, Integer otherId);

    /**
     * Удаление пользователя по идентификатору
     *
     * @param id Идентификатор пользователя
     */
    void deleteUserById(Integer id);

    /**
     * Поиск рекомендаций по идентификатору пользователя
     *
     * @param id Идентификатор пользователя
     * @return Список рекомендованных фильмов
     */
    List<Film> getRecommendations(Integer id);
}
