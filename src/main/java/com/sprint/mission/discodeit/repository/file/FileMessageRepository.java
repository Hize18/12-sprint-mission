package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.FileStorageException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FileLockProvider;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "12-sprint-mission.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {

  private final FileLockProvider fileLockProvider;
  private final Map<UUID, Message> data;
  private final Path path;

  public FileMessageRepository(
      FileLockProvider fileLockProvider,
      @Value("${12-sprint-mission.repository.file-directory:data}") String directory
  ) {
    this.fileLockProvider = fileLockProvider;
    path = Path.of(System.getProperty("user.dir"), directory, "message.ser");

    Map<UUID, Message> temp;
    ReentrantLock lock = this.fileLockProvider.getLock(this.path);

    lock.lock();
    try {
      Files.createDirectories(path.getParent());
      if (Files.exists(path)) {
        try (FileInputStream fis = new FileInputStream(path.toFile());
            ObjectInputStream ois = new ObjectInputStream(fis)) {
          temp = (Map<UUID, Message>) ois.readObject();
        }
      } else {
        temp = new HashMap<>();
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      temp = new HashMap<>();
    } finally {
      lock.unlock();
    }

    this.data = temp;
  }

  private void saveMap(Map<UUID, Message> map) {
    ReentrantLock lock = this.fileLockProvider.getLock(this.path);
    lock.lock();

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
      oos.writeObject(map);
    } catch (Exception e) {
      throw new FileStorageException("message file save failed.");
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Message save(Message message) {
    Map<UUID, Message> temp = new HashMap<>(data);

    temp.put(message.getId(), message);
    saveMap(temp);
    data.put(message.getId(), message);
    return message;
  }

  @Override
  public Optional<Message> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<Message> findByChannelId(UUID channelId) {
    List<Message> list = new ArrayList<>();

    for (Message value : data.values()) {
      if (Objects.equals(value.getChannelId(), channelId)) {
        list.add(value);
      }
    }
    list.sort(Comparator.comparing(Message::getUpdatedAt));
    return list;
  }

  @Override
  public List<Message> findByUserId(UUID userId) {
    List<Message> list = new ArrayList<>();

    for (Message value : data.values()) {
      if (Objects.equals(value.getAuthorId(), userId)) {
        list.add(value);
      }
    }
    list.sort(Comparator.comparing(Message::getUpdatedAt));
    return list;
  }

  @Override
  public List<Message> findAll() {
    List<Message> list = new ArrayList<>(data.values());
    list.sort(Comparator.comparing(Message::getUpdatedAt));
    return list;
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public void delete(UUID id) {
    Map<UUID, Message> temp = new HashMap<>(data);

    temp.remove(id);
    saveMap(temp);
    data.remove(id);
  }
}
