package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.FileStorageException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "12-sprint-mission.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

  private final FileLockProvider fileLockProvider;
  private final Map<UUID, BinaryContent> data;
  private final Path path;

  public FileBinaryContentRepository(
      FileLockProvider fileLockProvider,
      @Value("${12-sprint-mission.repository.file-directory:data}") String directory
  ) {
    this.fileLockProvider = fileLockProvider;
    this.path = Path.of(System.getProperty("user.dir"), directory, "binarycontent.ser");

    Map<UUID, BinaryContent> temp;
    ReentrantLock lock = this.fileLockProvider.getLock(this.path);

    lock.lock();
    try {
      Files.createDirectories(path.getParent());
      if (Files.exists(path)) {
        try (FileInputStream fis = new FileInputStream(path.toFile());
            ObjectInputStream ois = new ObjectInputStream(fis)) {
          temp = (Map<UUID, BinaryContent>) ois.readObject();
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

  private void saveMap(Map<UUID, BinaryContent> map) {
    ReentrantLock lock = this.fileLockProvider.getLock(this.path);

    lock.lock();
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
      oos.writeObject(map);
    } catch (Exception e) {
      throw new FileStorageException("binaryContent file save failed.");
    } finally {
      lock.unlock();
    }
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    Map<UUID, BinaryContent> temp = new HashMap<>(data);

    temp.put(binaryContent.getId(), binaryContent);
    saveMap(temp);
    data.put(binaryContent.getId(), binaryContent);
    return binaryContent;
  }

  @Override
  public Optional<BinaryContent> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<BinaryContent> findAll() {
    List<BinaryContent> list = new ArrayList<>(data.values());
    list.sort(Comparator.comparing(BinaryContent::getCreatedAt));
    return list;
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public void delete(UUID id) {
    Map<UUID, BinaryContent> temp = new HashMap<>(data);

    temp.remove(id);
    saveMap(temp);
    data.remove(id);
  }
}
