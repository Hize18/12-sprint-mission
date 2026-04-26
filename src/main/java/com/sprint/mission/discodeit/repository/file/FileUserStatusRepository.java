package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "12-sprint-mission.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {

  private final Map<UUID, UserStatus> data;
  private final Path path;

  public FileUserStatusRepository(
      @Value("${12-sprint-mission.repository.file-directory:data}") String directory
  ) {
    path = Path.of(System.getProperty("user.dir"), directory, "userstatus.ser");

    Map<UUID, UserStatus> temp;

    try {
      Files.createDirectories(path.getParent());
      if (Files.exists(path)) {
        try (FileInputStream fis = new FileInputStream(path.toFile());
            ObjectInputStream ois = new ObjectInputStream(fis)) {
          temp = (Map<UUID, UserStatus>) ois.readObject();
        }
      } else {
        temp = new HashMap<>();
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      temp = new HashMap<>();
    }
    this.data = temp;
  }

  private void saveMap(Map<UUID, UserStatus> map) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
      oos.writeObject(map);
    } catch (Exception e) {
      throw new IllegalStateException("userStatus file save failed.", e);
    }
  }

  @Override
  public UserStatus save(UserStatus userStatus) {
    Map<UUID, UserStatus> temp = new HashMap<>(data);

    temp.put(userStatus.getId(), userStatus);
    saveMap(temp);
    data.put(userStatus.getId(), userStatus);
    return userStatus;
  }

  @Override
  public Optional<UserStatus> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return data.values().stream()
        .filter(us -> us.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    List<UserStatus> list = new ArrayList<>(data.values());
    list.sort(Comparator.comparing(UserStatus::getUpdatedAt));
    return list;
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public void delete(UUID id) {
    Map<UUID, UserStatus> temp = new HashMap<>(data);

    temp.remove(id);
    saveMap(temp);
    data.remove(id);
  }
}
