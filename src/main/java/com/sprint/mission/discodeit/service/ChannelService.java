package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    boolean isUniqueName(String name);

    Channel save(Channel channel);

    Channel findById(UUID id);

    Channel findByName(String name);

    List<Channel> findByOwner(UUID ownerId);

    List<Channel> findAll();

    boolean update(UUID userId, UUID channelId, String name);

    boolean delete(UUID userId, UUID channelId);
}