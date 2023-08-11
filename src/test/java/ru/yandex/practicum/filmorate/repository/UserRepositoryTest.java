package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testing UserRepository class")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    private final UserRepositoryImpl userRepository;

    @Test
    @DisplayName("Testing insert user")
    void insertUser() {
        User user = userRepository.insertUser(User.builder()
                .email("ivan@mail.com")
                .login("van")
                .name("Ivan")
                .birthday(LocalDate.of(2000, 9, 10))
                .build());

        assertNotNull(user.getId());
        assertEquals("ivan@mail.com", user.getEmail());
        assertEquals("van", user.getLogin());
        assertEquals("Ivan", user.getName());
    }

    @Test
    @DisplayName("Testing update user")
    void updateUser() {
        User user = userRepository.updateUser(User.builder()
                .id(1)
                .email("tess@mail.com")
                .login("tessa")
                .name("Tess")
                .birthday(LocalDate.of(2000, 10, 10))
                .build());

        assertEquals(1, user.getId());
        assertEquals("tessa", user.getLogin());
        assertEquals("tess@mail.com", user.getEmail());
        assertEquals("Tess", user.getName());
    }

    @Test
    @DisplayName("Testing get all users")
    void getAllUsers() {
        List<User> users = userRepository.getAllUsers();

        assertNotNull(users);
    }

    @Test
    @DisplayName("Testing get user by id")
    void getUserById() {
        Optional<User> userOptional = userRepository.getUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    @DisplayName("Testing add friend to user")
    void addFriend() {
        User user = userRepository.addFriend(5, 1);

        assertEquals(1, user.getFriends().size());
        assertTrue(user.getFriends().stream().anyMatch(fr -> Objects.equals(fr.getId(), 1)));
    }

    @Test
    @DisplayName("Testing get all friends")
    void getAllFriends() {
        List<User> friends = userRepository.getAllFriends(1);

        assertNotNull(friends);
    }

    @Test
    @DisplayName("Testing get common friends")
    void getCommonFriends() {
        List<User> commonFriends = userRepository.getCommonFriends(1, 3);

        assertNotNull(commonFriends);
    }

    @Test
    @DisplayName("Testing delete friend")
    void deleteFriend() {
        List<User> users = userRepository.getAllFriends(4);
        Integer friendId = users.get(0).getId();
        User user = userRepository.deleteFriend(4, friendId);

        assertEquals(0, user.getFriends().size());
        assertTrue(user.getFriends().stream().noneMatch(friend -> Objects.equals(friend.getId(), friendId)));
    }

    @Test
    @DisplayName("Testing recommendation films for user")
    void getRecommendation() {
        List<Film> films = userRepository.getRecommendations(2);
        assertNotNull(films);

        Film film = films.get(0);
        assertEquals(2, film.getId());

        List<Film> emptyFilms = userRepository.getRecommendations(3);
        assertNotNull(emptyFilms);
        assertEquals(emptyFilms.size(), 0);
    }

    @Test
    @DisplayName("Testing user exist")
    void doesExist() {
        assertTrue(userRepository.doesExist(1));
    }
}