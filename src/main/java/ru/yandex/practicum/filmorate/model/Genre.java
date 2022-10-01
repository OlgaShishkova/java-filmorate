package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Genre {
    private int id;
    @NotBlank
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
