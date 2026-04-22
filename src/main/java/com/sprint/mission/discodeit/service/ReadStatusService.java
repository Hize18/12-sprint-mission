package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdate;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponse create(ReadStatusCreateRequest request);

    ReadStatusResponse findById(UUID id);

    List<ReadStatusResponse> findAllByUserId(UUID userId);

    void update(ReadStatusUpdate request);

    boolean delete(UUID id);
}
