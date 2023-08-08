package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    private final UserRepositoryImpl userRepository;
    private final FilmRepositoryImpl filmRepository;
    private final RatingService ratingService;

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

        assertNotNull(user.getId());
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

        assertNotNull(users);
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
        assertTrue(user.getFriends().stream().anyMatch(fr -> Objects.equals(fr.getId(), friend.getId())));
    }

    @Test
    @Order(6)
    void getAllFriends() {
        List<User> friends = userRepository.getAllFriends(1);

        assertNotNull(friends);
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

        assertNotNull(commonFriends);
    }

    @Test
    @Order(8)
    void deleteFriend() {
        List<User> users = userRepository.getAllFriends(1);
        Integer friendId = users.get(0).getId();
        User user = userRepository.deleteFriend(1, friendId);

        assertEquals(0, user.getFriends().size());
        assertTrue(user.getFriends().stream().noneMatch(friend -> Objects.equals(friend.getId(), friendId)));
    }

    @Test
    @Order(9)
    void doesExist() {
        assertTrue(userRepository.doesExist(1));
    }

    @Test
    @Order(10)
    void getRecommendationsTest(){
        Film film = filmRepository.insertFilm(Film.builder()
                .id(1)
                .name("NameTest1")
                .description("DescriptionTest1")
                .releaseDate(LocalDate.now())
                .duration(20L)
                .mpa(ratingService.getRatingById(1))
                .build());
        Film film2 = filmRepository.insertFilm(Film.builder()
                .id(2)
                .name("NameTest2")
                .description("DescriptionTest2")
                .releaseDate(LocalDate.now())
                .duration(20L)
                .mpa(ratingService.getRatingById(1))
                .build());
        Film film3 = filmRepository.insertFilm(Film.builder()
                .id(3)
                .name("NameTest3")
                .description("DescriptionTest3")
                .releaseDate(LocalDate.now())
                .duration(20L)
                .mpa(ratingService.getRatingById(1))
                .build());
        User user = userRepository.insertUser(User.builder()
                .id(11)
                .email(EMAIL)
                .login("TestLogin")
                .name(NAME)
                .birthday(LocalDate.of(2000, 9, 10))
                .build());
        User user2 = userRepository.insertUser(User.builder()
                .id(12)
                .email(EMAIL)
                .login("TestLogin2")
                .name(NAME)
                .birthday(LocalDate.of(2000, 9, 10))
                .build());
        User user3 = userRepository.insertUser(User.builder()
                .id(13)
                .email(EMAIL)
                .login("TestLogin3")
                .name(NAME)
                .birthday(LocalDate.of(2000, 9, 10))
                .build());
        filmRepository.putLike(1,1,6);
        filmRepository.putLike(1,2,5);
        filmRepository.putLike(2,2,7);
        filmRepository.putLike(3,2,2);
        List<Film> films = userRepository.getRecommendations(1);
       assertEquals( userRepository.getRecommendations(1).size(),1);






    }
}