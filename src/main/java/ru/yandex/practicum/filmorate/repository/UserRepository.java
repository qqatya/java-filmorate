package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {
    /**
     * Создание/обновление пользователя
     *
     * @param user Объект, содержащий данные для создания
     * @return Созданный пользователь
     */
    User insertUser(User user);

    /**
     * Получение всех пользователей
     *
     * @return Список пользователей
     */
    List<User> getAllUsers();
}
