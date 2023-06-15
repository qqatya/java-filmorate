package ru.yandex.practicum.filmorate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public User insertUser(User user) {
        user.setId(++idCounter);
        log.info("Creating user with id = {}", user.getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) != null) {
            users.put(user.getId(), user);
        }
        return users.get(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return users.values().stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = users.get(userId);
        Set<Integer> userFriendIds = user.getFriends();

        userFriendIds.add(friendId);
        user.setFriends(userFriendIds);
        users.put(user.getId(), user);
        log.debug("UserId = {} friends list: {}", userId, userFriendIds);
        User friend = users.get(friendId);
        Set<Integer> friendFriendIds = friend.getFriends();

        friendFriendIds.add(userId);
        friend.setFriends(friendFriendIds);
        users.put(friend.getId(), friend);
        log.debug("UserId = {} friends list: {}", friendId, friendFriendIds);
        return users.get(userId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = users.get(userId);
        Set<Integer> userFriendIds = user.getFriends();

        userFriendIds.remove(friendId);
        user.setFriends(userFriendIds);
        users.put(user.getId(), user);
        log.debug("UserId = {} friends list: {}", userId, userFriendIds);
        User friend = users.get(friendId);
        Set<Integer> friendFriendIds = friend.getFriends();

        friendFriendIds.remove(userId);
        friend.setFriends(friendFriendIds);
        users.put(friend.getId(), friend);
        log.debug("UserId = {} friends list: {}", friendId, friendFriendIds);
        return users.get(userId);
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        return users.get(id).getFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        Set<Integer> userFriendIds = users.get(userId).getFriends();
        Set<Integer> otherFriendIds = users.get(otherId).getFriends();
        Set<Integer> intersection = new HashSet<>();

        intersection.addAll(userFriendIds);
        intersection.addAll(otherFriendIds);
        Set<Integer> result = intersection.stream()
                .filter(id -> !Objects.equals(id, userId) && !Objects.equals(id, otherId))
                .collect(Collectors.toSet());
        log.debug("Common friends list: {}", result);
        return users.values().stream()
                .filter(user -> result.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean doesExist(Integer id) {
        return users.keySet().stream().anyMatch(userId -> Objects.equals(userId, id));
    }

}
