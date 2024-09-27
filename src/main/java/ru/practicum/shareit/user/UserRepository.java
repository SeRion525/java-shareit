package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {
    List<User> getAll();

    Optional<User> getById(long userId);

    User save(User user);

    User update(User user);

    void delete(long userId);

    Set<String> getEmails();
}
