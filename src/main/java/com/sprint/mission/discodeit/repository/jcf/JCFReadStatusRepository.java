package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name ="12-sprint-mission.repository.type", havingValue = "jcf")
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository() {
        data = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
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
            if (value.getUserId().equals(userId)) list.add(value);
        }
        list.sort(Comparator.comparing(ReadStatus::getUpdatedAt));
        return list;
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        List<ReadStatus> list = new ArrayList<>();

        for (ReadStatus value : data.values()) {
            if (value.getChannelId().equals(channelId)) list.add(value);
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
        data.remove(id);
    }
}
