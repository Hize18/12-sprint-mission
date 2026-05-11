package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    BinaryContent binaryContent = new BinaryContent(request.fileName(), request.contentType(),
        request.bytes());
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent findById(UUID binaryContentId) {
    if (binaryContentId == null) {
      throw new IllegalArgumentException("binaryContentId is null.");
    }

    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NotFoundException("binaryContent not found"));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    if (binaryContentIds == null) {
      throw new IllegalArgumentException("binaryContentIds is null.");
    }

    return binaryContentIds.stream()
        .filter(Objects::nonNull)
        .map(id -> binaryContentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not found: " + id)))
        .toList();
  }

  @Override
  public void delete(UUID binaryContentId) {
    if (binaryContentId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new NotFoundException("binaryContent not found");
    }
    binaryContentRepository.delete(binaryContentId);
  }
}
