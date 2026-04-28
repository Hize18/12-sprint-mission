package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

  private final Map<UUID, Channel> data;

  public JCFChannelRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public Channel save(Channel channel) {
    data.put(channel.getId(), channel);
    return channel;
  }

  @Override
  public Optional<Channel> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<Channel> findAll() {
    List<Channel> list = new ArrayList<>(data.values());
    list.sort(Comparator.comparing(Channel::getUpdatedAt));
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
