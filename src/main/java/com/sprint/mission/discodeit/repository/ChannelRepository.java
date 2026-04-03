package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Channel findById(UUID id);
    Channel findByHandle(String handle);
    List<Channel> findByOwner(UUID ownerId);
    List<Channel> findByName(String name);
    List<Channel> findAll();
    void delete(UUID id);
}
