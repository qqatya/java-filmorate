# java-filmorate
ER diagram:

![](/Users/buurgr/Downloads/filmorate_er.png)

Query examples:

DELETE FROM film_like 
WHERE film_id = 1 AND liked_person_id = 3;

SELECT * FROM genre;

SELECT COUNT(*) FROM rating;

INSERT INTO person (email, login, name, birthday)
VALUES('test@ya.ru', 'cattt', 'Murzik', '2000-09-09');