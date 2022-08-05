package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы.")
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;
}
