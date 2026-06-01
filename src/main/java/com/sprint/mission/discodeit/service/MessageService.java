package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDto create(MessageCreateRequest request);

  MessageDto findDetailById(UUID messageId);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable);

  MessageDto update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
