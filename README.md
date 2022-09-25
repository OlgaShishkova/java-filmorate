# java-filmorate

![filmorate_db](https://user-images.githubusercontent.com/102627749/192141595-b2826bb5-fa61-4ac3-8e7d-4bbc7aabc499.png)

Примеры запросов

Получение всех фильмов:

SELECT name

FROM film;


Получение топ-N популярных фильмов:

SELECT name,
       likes
       
FROM film

ORDER BY likes DESC

LIMIT N;

Получение списка друзей у пользователя N:

SELECT u.login

FROM user AS u

INNER JOIN friendship_status AS f ON f.friend_id = u.user_id


WHERE f.user_id = 'N';
