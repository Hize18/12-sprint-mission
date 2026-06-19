package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> attachmentRequests);

  MessageDto findDetailById(UUID messageId);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable);

  MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest,
      List<BinaryContentCreateRequest> attachmentRequests);

  void delete(UUID messageId);
}
