INSERT INTO person (email, login, name, birthday) VALUES ('tess@mail.com', 'testy', 'Tess', '2000-10-10');
INSERT INTO person (email, login, name, birthday) VALUES ( 'bob@mail.com', 'bobby', 'Robert', '1990-01-01');
INSERT INTO person (email, login, name, birthday) VALUES ( 'sam@mail.com', 'sammy', 'Samanta', '1980-12-12');
INSERT INTO person (email, login, name, birthday) VALUES ( 'jane@mail.com', 'janny', 'Jane', '1990-11-11');
INSERT INTO person (email, login, name, birthday) VALUES ( 'hhh@mail.com', 'harry', 'Harry', '1985-02-02');
INSERT INTO person (email, login, name, birthday) VALUES ( 'milly@mail.com', 'miller', 'Miller', '1999-07-07');

INSERT INTO film (name, description, release_date, duration, rating_id) VALUES ('Leon', 'About killer', '1984-12-14', 110, 5);
INSERT INTO film (name, description, release_date, duration, rating_id) VALUES ('La-la Land', 'Love story', '2017-01-12', 120, 4);
INSERT INTO film (name, description, release_date, duration, rating_id) VALUES ('The Shining', 'Horror film', '1980-05-23', 150, 5);
INSERT INTO film (name, description, release_date, duration, rating_id) VALUES ('Frozen', 'Disney animation', '2013-12-12', 100, 1);
INSERT INTO film (name, description, release_date, duration, rating_id) VALUES ('Barbi', 'Pink comedy', '2023-07-09', 115, 3);
INSERT INTO film (name, description, release_date, duration, rating_id) VALUES ('Bezos', 'Biography', '2023-01-24', 90, 3);

INSERT INTO friendship (person_id, friend_id, is_confirmed) VALUES (1, 2, FALSE);
INSERT INTO friendship (person_id, friend_id, is_confirmed) VALUES (2, 1, TRUE);
INSERT INTO friendship (person_id, friend_id, is_confirmed) VALUES (3, 2, FALSE);
INSERT INTO friendship (person_id, friend_id, is_confirmed) VALUES (2, 3, TRUE);
INSERT INTO friendship (person_id, friend_id, is_confirmed) VALUES (1, 4, FALSE);
INSERT INTO friendship (person_id, friend_id, is_confirmed) VALUES (4, 1, TRUE);

INSERT INTO film_like (film_id, liked_person_id, grade) VALUES (1, 2, 6);
INSERT INTO film_like (film_id, liked_person_id, grade) VALUES (2, 4, 7);
INSERT INTO film_like (film_id, liked_person_id, grade) VALUES (5, 4, 2);
INSERT INTO film_like (film_id, liked_person_id, grade) VALUES (3, 5, 8);
INSERT INTO film_like (film_id, liked_person_id, grade) VALUES (4, 1, 8);
INSERT INTO film_like (film_id, liked_person_id, grade) VALUES (1, 4, 6);
INSERT INTO film_like (film_id, liked_person_id, grade) VALUES (5, 1, 9);
INSERT INTO film_like (film_id, liked_person_id, grade) VALUES (5, 5, 9);

INSERT INTO film_genre (film_id, genre_id) VALUES (1, 6);
INSERT INTO film_genre (film_id, genre_id) VALUES (1, 2);
INSERT INTO film_genre (film_id, genre_id) VALUES (2, 2);
INSERT INTO film_genre (film_id, genre_id) VALUES (3, 4);
INSERT INTO film_genre (film_id, genre_id) VALUES (4, 3);
INSERT INTO film_genre (film_id, genre_id) VALUES (5, 1);
INSERT INTO film_genre (film_id, genre_id) VALUES (5, 2);
INSERT INTO film_genre (film_id, genre_id) VALUES (6, 2);

INSERT INTO director (name) VALUES ('Tom Land');
INSERT INTO director (name) VALUES ('Bob Soul');
INSERT INTO director (name) VALUES ('Leon Smith');

INSERT INTO film_director (film_id, director_id) VALUES (1, 1);
INSERT INTO film_director (film_id, director_id) VALUES (2, 2);
INSERT INTO film_director (film_id, director_id) VALUES (3, 2);
INSERT INTO film_director (film_id, director_id) VALUES (4, 2);
INSERT INTO film_director (film_id, director_id) VALUES (5, 3);
INSERT INTO film_director (film_id, director_id) VALUES (6, 2);