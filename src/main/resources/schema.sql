DROP ALL OBJECTS;
CREATE TABLE IF NOT EXISTS director (
                                id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                name VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS genre (
                                id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                name VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS rating (
                                id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                name VARCHAR(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS film (
                                id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                description VARCHAR(200),
                                release_date DATE,
                                duration BIGINT,
                                rating_id INTEGER REFERENCES rating (id)
);

CREATE TABLE IF NOT EXISTS film_genre (
                                film_id INTEGER REFERENCES film (id) ON DELETE CASCADE,
                                genre_id INTEGER REFERENCES genre (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_director (
                                film_id INTEGER REFERENCES film (id) ON DELETE CASCADE,
                                director_id INTEGER REFERENCES director (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS person (
                                id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                email VARCHAR(320) NOT NULL,
                                login VARCHAR(100) NOT NULL,
                                name VARCHAR(150) NOT NULL,
                                birthday DATE
);

CREATE TABLE IF NOT EXISTS film_like (
                                film_id INTEGER REFERENCES film (id) ON DELETE CASCADE,
                                liked_person_id INTEGER REFERENCES person (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendship (
                                person_id INTEGER REFERENCES person (id) ON DELETE CASCADE,
                                friend_id INTEGER REFERENCES person (id) ON DELETE CASCADE,
                                is_confirmed BOOLEAN
);

CREATE TABLE IF NOT EXISTS review (
                                id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                content VARCHAR(3000),
                                is_positive BOOLEAN,
                                user_id INTEGER REFERENCES person (id) ON DELETE CASCADE,
                                film_id INTEGER REFERENCES film (id) ON DELETE CASCADE,
                                useful INTEGER
);

CREATE TABLE IF NOT EXISTS review_rate_user (
                                         review_id INTEGER REFERENCES review (id) ON DELETE CASCADE,
                                         rated_user_id INTEGER REFERENCES person (id) ON DELETE CASCADE,
                                         is_positive BOOLEAN
);