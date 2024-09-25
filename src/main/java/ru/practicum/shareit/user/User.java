package ru.practicum.shareit.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;
    private String name;
    private String email;
}
