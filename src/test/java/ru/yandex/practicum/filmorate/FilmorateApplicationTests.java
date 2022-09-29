package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = {"classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)

class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    @Test
    public void testFindUserById() {
        List<User> users = userStorage.findAll();
        Optional<User> userOptional = userStorage.findById(users.get(0).getId());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "userName1")
                );
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.findAll();
        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    public void testDeleteUser() {
        List<User> users = userStorage.findAll();
        userStorage.delete(users.get(0).getId());
        assertThat(userStorage.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testFindAllFilms() {
        List<Film> films = filmStorage.findAll();
        assertThat(films.size()).isEqualTo(3);
    }

    @Test
    public void testFindFilmById() {
        List<Film> films = filmStorage.findAll();
        Optional<Film> filmOptional = filmStorage.findById(films.get(0).getId());
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "film1")
                );
    }

    @Test
    public void testDeleteFilm() {
        List<Film> films = filmStorage.findAll();
        filmStorage.remove(films.get(0).getId());
        assertThat(filmStorage.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testGetGenreById() {
        Optional<Genre> genreOptional = filmStorage.getGenreById(1);
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

//    @Test
//    public void testAddLike() {
//        long userIndex = userStorage.findAll().get(0).getId();
//        int filmIndex = filmStorage.findAll().get(0).getId();
//        assertThat(filmStorage.addLike(filmIndex, userIndex)).isEqualTo(1);
//    }

//    @Test
//    public void testGetPopular() {
//        long userIndex = userStorage.findAll().get(0).getId();
//        int filmIndex = filmStorage.findAll().get(0).getId();
//        filmStorage.addLike(filmIndex, userIndex);
//        List<Film> popularFilms = filmStorage.getPopular(1);
//        assertThat(popularFilms.size()).isEqualTo(1);
//        assertThat(popularFilms.get(0)).hasFieldOrPropertyWithValue("name", "film1");
//        filmStorage.addLike(filmIndex + 1, userIndex);
//        filmStorage.addLike(filmIndex + 1, userIndex + 1);
//        popularFilms = filmStorage.getPopular(10);
//        assertThat(popularFilms.get(0)).hasFieldOrPropertyWithValue("name", "film2");
//    }
}
