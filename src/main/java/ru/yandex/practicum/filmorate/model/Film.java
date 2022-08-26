package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max=200, message = "Максимальная длина описания 200 символов")
    @EqualsAndHashCode.Exclude
    private String description;
    @NotNull(message = "Дата релиза не может быть пустой")
    @DateAfter(minDate = "28.12.1895", message = "Дата релиза должна быть позже 28.12.1895")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
    @EqualsAndHashCode.Exclude
    private Set<Long> likes = new HashSet<>();
}
