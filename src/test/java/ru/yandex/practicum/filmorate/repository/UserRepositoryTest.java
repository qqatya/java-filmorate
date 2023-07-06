package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    private final UserRepositoryImpl userRepository;

    private static final String EMAIL = "ivan@ya.ru";
    private static final String LOGIN = "iwwwwan";
    private static final String NAME = "Ivan Ivanov";

    @Test
    @Order(1)
    void insertUser() {
        User user = userRepository.insertUser(User.builder()
                .email(EMAIL)
                .login(LOGIN)
                .name(NAME)
                .birthday(LocalDate.of(2000, 9, 10))
                .build());

        assertEquals(1, user.getId());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(LOGIN, user.getLogin());
        assertEquals(NAME, user.getName());
    }

    @Test
    @Order(2)
    void updateUser() {
        String updLogin = "iwwanUPD";

        User user = userRepository.updateUser(User.builder()
                .id(1)
                .email(EMAIL)
                .login(updLogin)
                .name(NAME)
                .birthday(LocalDate.of(2000, 9, 10))
                .build());

        assertEquals(1, user.getId());
        assertEquals(updLogin, user.getLogin());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(NAME, user.getName());
    }

    @Test
    @Order(3)
    void getAllUsers() {
        List<User> users = userRepository.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());
    }

    @Test
    @Order(4)
    void getUserById() {
        Optional<User> userOptional = userRepository.getUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @Order(5)
    void addFriend() {
        User friend = userRepository.insertUser(User.builder()
                .email("friend@email.com")
                .login("friend")
                .name("Friend")
                .birthday(LocalDate.of(2000, 10, 10))
                .build());

        User user = userRepository.addFriend(1, friend.getId());

        assertEquals(1, user.getFriends().size());
        assertTrue(user.getFriends().stream().anyMatch(fr -> fr.getId() == 2));
    }

    @Test
    @Order(6)
    void getAllFriends() {
        List<User> friends = userRepository.getAllFriends(1);

        assertEquals(1, friends.size());
        assertEquals(2, friends.get(0).getId());
    }

    @Test
    @Order(7)
    void getCommonFriends() {
        User common = userRepository.insertUser(User.builder()
                .email("common@email.com")
                .login("common")
                .name("Common")
                .birthday(LocalDate.of(2001, 10, 10))
                .build());
        User user = userRepository.addFriend(common.getId(), 2);

        List<User> commonFriends = userRepository.getCommonFriends(user.getId(), 1);

        assertEquals(1, commonFriends.size());
        assertEquals(2, commonFriends.get(0).getId());
    }

    @Test
    @Order(8)
    void deleteFriend() {
        User user = userRepository.deleteFriend(1, 2);

        assertEquals(0, user.getFriends().size());
        assertTrue(user.getFriends().stream().noneMatch(friend -> friend.getId() == 2));
    }

    @Test
    @Order(9)
    void doesExist() {
        assertTrue(userRepository.doesExist(1));
    }
}