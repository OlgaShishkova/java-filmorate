# java-filmorate

![filmorate_db](https://user-images.githubusercontent.com/102627749/192586189-f628056b-4345-453b-9e93-6dea25773c18.png)


Примеры запросов

Получение всех фильмов:

SELECT name
FROM films;

Получение топ-N популярных фильмов:

SELECT f.name,
       count(fl.user_id) as likes       
FROM films as f
LEFT JOIN film_likes as fl ON f.film_id = fl.film_id
GROUP BY f.name
ORDER BY likes DESC
LIMIT N;

Получение списка друзей у пользователя N:

SELECT u.login
FROM users AS u
INNER JOIN user_friends AS uf ON uf.friend_id = u.user_id
WHERE uf.user_id = 'N';
