package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusResponse create(ReadStatusCreateRequest request);

  ReadStatusResponse findById(UUID id);

  ReadStatusResponse findByUserIdAndChannelId(UUID userId, UUID channelId);

  List<ReadStatusResponse> findAllByUserId(UUID userId);

  ReadStatusResponse update(UUID userId, UUID channelId);

  void delete(UUID id);
}
