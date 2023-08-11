package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FriendMapper;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.repository.FriendRepository;

import java.util.HashSet;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final FriendMapper friendMapper;

    @Override
    public Set<Friend> getFriendsByUserId(Integer id) {
        String sqlGetFriendsByUserId = "SELECT friend_id, is_confirmed FROM public.friendship "
                + "WHERE person_id = :id UNION SELECT person_id, is_confirmed FROM public.friendship "
                + "WHERE friend_id = :id AND is_confirmed = true";
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return new HashSet<>(jdbcTemplate.query(sqlGetFriendsByUserId, params, friendMapper));
    }
}
