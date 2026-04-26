package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileReadStatusRepository implements ReadStatusRepository {

  private final Map<UUID, ReadStatus> data;
  private final Path path;

  public FileReadStatusRepository(
      @Value("${12-sprint-mission.repository.file-directory:data}") String directory
  ) {
    path = Path.of(System.getProperty("user.dir"), directory, "readstatus.ser");

    Map<UUID, ReadStatus> temp;

    try {
      Files.createDirectories(path.getParent());
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)) {
                temp = (Map<UUID, ReadStatus>) ois.readObject();
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

  private void saveMap(Map<UUID, ReadStatus> map) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
      oos.writeObject(map);
    } catch (Exception e) {
      throw new IllegalStateException("readStatus file save failed.", e);
    }
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    Map<UUID, ReadStatus> temp = new HashMap<>(data);

    temp.put(readStatus.getId(), readStatus);
    saveMap(temp);
    data.put(readStatus.getId(), readStatus);
    return readStatus;
  }

  @Override
  public Optional<ReadStatus> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
    return data.values().stream()
        .filter(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
        .findFirst();
  }

  @Override
  public List<ReadStatus> findByUserId(UUID userId) {
    List<ReadStatus> list = new ArrayList<>();

    for (ReadStatus value : data.values()) {
        if (value.getUserId().equals(userId)) {
            list.add(value);
        }
    }
    list.sort(Comparator.comparing(ReadStatus::getUpdatedAt));
    return list;
  }

  @Override
  public List<ReadStatus> findByChannelId(UUID channelId) {
    List<ReadStatus> list = new ArrayList<>();

    for (ReadStatus value : data.values()) {
        if (value.getChannelId().equals(channelId)) {
            list.add(value);
        }
    }
    list.sort(Comparator.comparing(ReadStatus::getUpdatedAt));
    return list;
  }

  @Override
  public List<ReadStatus> findAll() {
    List<ReadStatus> list = new ArrayList<>(data.values());
    list.sort(Comparator.comparing(ReadStatus::getUpdatedAt));
    return list;
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public void delete(UUID id) {
    Map<UUID, ReadStatus> temp = new HashMap<>(data);

    temp.remove(id);
    saveMap(temp);
    data.remove(id);
  }
}
