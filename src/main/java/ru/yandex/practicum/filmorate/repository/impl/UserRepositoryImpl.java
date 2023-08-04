package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.FriendRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final UserMapper userMapper;

    private final FilmRepository filmRepository;

    private final FriendRepository friendRepository;

    private static final String SQL_INSERT_USER = "INSERT INTO public.person "
            + "(email, login, name, birthday) VALUES(:email, :login, :name, :birthday)";

    private static final String SQL_UPDATE_USER = "UPDATE public.person SET email = :email, login = :login, "
            + "name = :name, birthday = :birthday where id = :id";

    private static final String SQL_GET_USER_BY_ID = "SELECT id, email, login, name, birthday FROM public.person "
            + "WHERE id = :id";

    private static final String SQL_GET_ALL_USERS = "SELECT id, email, login, name, birthday FROM public.person";

    private static final String SQL_INSERT_FRIEND = "INSERT INTO public.friendship (person_id, friend_id, is_confirmed) "
            + "VALUES (:person_id, :friend_id, :is_confirmed)";

    private static final String SQL_CONFIRM_FRIENDSHIP = "UPDATE public.friendship SET is_confirmed = true "
            + "WHERE (person_id = :person_id AND friend_id = :friend_id) "
            + "OR (person_id = :friend_id AND friend_id = :person_id)";

    private static final String SQL_DELETE_FRIEND = "DELETE FROM public.friendship "
            + "WHERE (person_id = :person_id AND friend_id = :friend_id) "
            + "OR (person_id = :friend_id AND friend_id = :person_id and is_confirmed = true)";

    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM public.person WHERE id = :id";

    private static final String SQL_GET_SIMILAR_USER = "SELECT l.liked_person_id FROM public.film_like AS l " +
            "WHERE l.film_id IN " +
            "(SELECT l2.film_id FROM public.film_like AS l2 WHERE l2.liked_person_id = :id) " +
            "AND l.liked_person_id != :id " +
            "GROUP BY l.liked_person_id " +
            "ORDER BY COUNT(l.film_id) DESC LIMIT 1";

    private static final String SQL_GET_RECOMMENDATIONS = "SELECT l.film_id FROM public.film_like AS l " +
            "WHERE l.liked_person_id = :other_id AND l.film_id NOT IN " +
            "(SELECT l2.film_id FROM public.film_like AS l2 WHERE l2.liked_person_id = :id)";

    @Override
    public User insertUser(User user) {
        log.info("Creating user with id = {}", user.getId());
        MapSqlParameterSource params = getParams(user);
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(SQL_INSERT_USER, params, holder);
        Integer userId = holder.getKey().intValue();

        return getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));
    }

    @Override
    public User updateUser(User user) {
        KeyHolder holder = new GeneratedKeyHolder();

        if (getUserById(user.getId()).isPresent()) {
            MapSqlParameterSource params = getParams(user);

            params.addValue("id", user.getId());
            jdbcTemplate.update(SQL_UPDATE_USER, params, holder);
        }
        Integer userId = holder.getKey().intValue();

        return getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(SQL_GET_ALL_USERS, userMapper);
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        return jdbcTemplate.query(SQL_GET_USER_BY_ID, params, userMapper).stream().findFirst();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        Set<Friend> friends = friendRepository.getFriendsByUserId(userId);
        var params = new MapSqlParameterSource();

        params.addValue("person_id", userId);
        params.addValue("friend_id", friendId);
        if (friends.stream().anyMatch(friend -> friend.getId().equals(friendId) && !friend.getIsConfirmed())) {
            jdbcTemplate.update(SQL_CONFIRM_FRIENDSHIP, params);
        }
        if (friends.stream().noneMatch(friend -> friend.getId().equals(friendId))) {
            params.addValue("is_confirmed", false);
            jdbcTemplate.update(SQL_INSERT_FRIEND, params);
        }
        return getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        var params = new MapSqlParameterSource();

        params.addValue("person_id", userId);
        params.addValue("friend_id", friendId);
        jdbcTemplate.update(SQL_DELETE_FRIEND, params);
        return getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        return friendRepository.getFriendsByUserId(id).stream()
                .map(friend -> getUserById(friend.getId())
                        .orElseThrow(() -> new UserNotFoundException(String.valueOf(friend.getId()))))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        Set<Integer> userFriendIds = friendRepository.getFriendsByUserId(userId).stream()
                .map(Friend::getId)
                .collect(Collectors.toSet());
        Set<Integer> otherFriendIds = friendRepository.getFriendsByUserId(otherId).stream().map(Friend::getId)
                .collect(Collectors.toSet());
        Set<Integer> intersection = new HashSet<>();

        intersection.addAll(userFriendIds);
        intersection.addAll(otherFriendIds);
        Set<Integer> result = intersection.stream()
                .filter(id -> !Objects.equals(id, userId) && !Objects.equals(id, otherId))
                .collect(Collectors.toSet());
        log.debug("Common friends list: {}", result);
        return result.stream()
                .map(friendId -> getUserById(friendId)
                        .orElseThrow(() -> new UserNotFoundException(String.valueOf(friendId))))
                .collect(Collectors.toList());
    }

    @Override
    public boolean doesExist(Integer id) {
        return getAllUsers().stream().anyMatch(user -> Objects.equals(user.getId(), id));
    }

    @Override
    public void deleteUserById(Integer id) {
        var params = new MapSqlParameterSource();

        params.addValue("id", id);
        jdbcTemplate.update(SQL_DELETE_USER_BY_ID, params);
    }

    @Override
    public List<Film> getRecommendations(Integer id) {
        var params = new MapSqlParameterSource();
        Optional<Integer> otherId = getSimilarUserId(id);
        if (otherId.isEmpty()) {
            return new ArrayList<>();
        }
        params.addValue("id", id);
        params.addValue("other_id", otherId.get());
        List<Integer> filmId = jdbcTemplate.queryForList(SQL_GET_RECOMMENDATIONS, params, Integer.class);
        return filmId.stream()
                .filter(i -> filmRepository.getFilmById(i).isPresent())
                .map(i -> filmRepository.getFilmById(i).orElseThrow(() -> new FilmNotFoundException(String.valueOf(i))))
                .collect(Collectors.toList());
    }

    private Optional<Integer> getSimilarUserId(Integer id) {
        List<Integer> ids = jdbcTemplate.queryForList(SQL_GET_SIMILAR_USER, Map.of("id", id), Integer.class);
        if (ids.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(ids.get(0));
    }

    private MapSqlParameterSource getParams(User user) {
        var params = new MapSqlParameterSource();

        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());
        return params;
    }
}
