drop table if exists mpa cascade;
drop table if exists films cascade;
drop table if exists genres cascade;
drop table if exists users cascade;
drop table if exists film_genre cascade;
drop table if exists film_likes cascade;
drop table if exists friendship_status cascade;

CREATE TABLE IF NOT EXISTS mpa (
                                      mpa_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                      mpa_name varchar(10)
);


CREATE TABLE IF NOT EXISTS films (
                                     id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     name varchar(40) NOT NULL,
                                     description varchar(200),
                                     release_date date NOT NULL,
                                     duration integer NOT NULL,
                                     mpa_id integer REFERENCES mpa(mpa_id)
);

CREATE TABLE IF NOT EXISTS genres (
                                      id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                      name varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
                                          id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                          film_id integer REFERENCES films (id),
                                          genre_id integer REFERENCES genres (id)
);

CREATE TABLE IF NOT EXISTS users (
                                     id integer GENERATED  BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     email varchar(40) NOT NULL,
                                     login varchar(20) NOT NULL,
                                     name varchar(20),
                                     birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS film_likes (
                                          id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                          film_id integer REFERENCES films (id),
                                          user_id integer REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS friendship_status (
                                                 id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                                 user_id integer REFERENCES users (id),
                                                 friend_id integer REFERENCES users (id),
                                                 is_confirmed boolean
);