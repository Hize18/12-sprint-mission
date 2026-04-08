package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message save(Message message);
    Message findById(UUID id);
    List<Message> findByChannelId(UUID channelId);
    List<Message> findByUserId(UUID userId);
    List<Message> findByKeyword(String keyword);
    List<Message> findAll();
    boolean update(UUID userId, UUID messageId, String content);
    boolean delete(UUID userId, UUID messageId);
}
