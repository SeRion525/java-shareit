package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, String> usersAndEmails = new HashMap<>();
    private long userCounter = 0;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User save(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        usersAndEmails.putIfAbsent(user.getId(), user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        usersAndEmails.put(user.getId(), user.getEmail());
        return user;
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
        usersAndEmails.remove(userId);
    }

    @Override
    public Set<String> getEmails() {
        return new HashSet<>(usersAndEmails.values());
    }

    private long generateId() {
        return ++userCounter;
    }
}
