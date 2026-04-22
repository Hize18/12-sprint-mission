package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponse create(MessageCreateRequest request);

    MessageResponse findById(UUID id);

    List<MessageResponse> findAllByChannelId(UUID channelId);

    List<MessageResponse> findByUserId(UUID userId);

    List<MessageResponse> findAll();

    boolean update(UUID userId, MessageUpdateRequest request);

    boolean delete(UUID userId, UUID messageId);
}
