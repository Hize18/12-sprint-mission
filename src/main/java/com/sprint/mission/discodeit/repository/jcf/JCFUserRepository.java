package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {

  private final Map<UUID, User> data;

  public JCFUserRepository() {
    data = new HashMap<>();
  }

  @Override
  public User save(User user) {
    data.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<User> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return data.values().stream()
        .filter(user -> Objects.equals(user.getUsername(), username))
        .findFirst();
  }

  @Override
  public List<User> findAll() {
    List<User> list = new ArrayList<>(data.values());
    list.sort(Comparator.comparing(User::getUpdatedAt));
    return list;
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public void delete(UUID id) {
    data.remove(id);
  }
}
