package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.Set;

public interface FriendRepository {
    /**
     * Получение друзей пользователя
     *
     * @param id Идентификатор пользователя
     * @return Список друзей
     */
    Set<Friend> getFriendsByUserId(Integer id);
}
