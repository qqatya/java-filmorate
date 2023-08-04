package ru.yandex.practicum.filmorate.repository;

import java.util.Set;

public interface UserLikeRepository {
    /**
     * Получение идентификаторов пользоваталей, лайкнувших фильм
     *
     * @param id Идентификатор фильма
     * @return Список идентификаторов пользователей
     */
    Set<Integer> getUsersLikedByFilmId(Integer id);
}
