package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(name ="12-sprint-mission.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data;
    private final Path path = Path.of(System.getProperty("user.dir"), "data_repo/binarycontent.ser");

    public FileBinaryContentRepository() {
        Map<UUID, BinaryContent> temp;

        try {
            Files.createDirectories(path.getParent());
            if (Files.exists(path)) {
                try (FileInputStream fis = new FileInputStream(path.toFile());
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    temp = (Map<UUID, BinaryContent>) ois.readObject();
                }
            } else temp = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            temp = new HashMap<>();
        }
        this.data = temp;
    }

    private void saveMap(Map<UUID, BinaryContent> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(map);
        } catch (Exception e) {
            throw new IllegalStateException("binaryContent file save failed.", e);
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
    public void delete(UUID id) {
        Map<UUID, BinaryContent> temp = new HashMap<>(data);

        temp.remove(id);
        saveMap(temp);
        data.remove(id);
    }
}
