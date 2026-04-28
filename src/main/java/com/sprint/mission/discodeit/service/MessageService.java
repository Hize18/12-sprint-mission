package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  Message create(MessageCreateRequest request);

  Message findById(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId);

  List<Message> findByUserId(UUID userId);

  List<Message> findAll();

  Message update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
