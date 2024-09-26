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
    private final Set<String> emails = new HashSet<>();
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
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public void delete(long userId) {
        User user = users.remove(userId);
        if (user != null) {
            emails.remove(user.getEmail());
        }
    }

    @Override
    public Set<String> getEmails() {
        return new HashSet<>(emails);
    }

    private long generateId() {
        return ++userCounter;
    }
}
