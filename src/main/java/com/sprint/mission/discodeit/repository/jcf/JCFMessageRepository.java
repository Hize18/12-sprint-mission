package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

  private final Map<UUID, Message> data;

  public JCFMessageRepository() {
    data = new HashMap<>();
  }

  @Override
  public Message save(Message message) {
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
      if (Objects.equals(value.getUserId(), userId)) {
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
    data.remove(id);
  }
}
