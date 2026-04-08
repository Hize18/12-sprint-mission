package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    boolean isUniqueHandle(String handle);
    String createHandle(String name);
    Channel save(Channel channel);
    Channel findById(UUID id);
    Channel findByHandle(String handle);
    List<Channel> findByOwner(UUID ownerId);
    List<Channel> findByName(String name);
    List<Channel> findAll();
    boolean update(UUID userId, UUID channelId, String name);
    boolean delete(UUID userId, UUID channelId);
}