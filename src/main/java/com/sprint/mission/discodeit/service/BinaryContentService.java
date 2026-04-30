package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponse;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponse create(BinaryContentCreateRequest request);

    BinaryContentResponse findById(UUID id);

    List<BinaryContentResponse> findAllByIdIn(List<UUID> idList);

    void delete(UUID id);
}
