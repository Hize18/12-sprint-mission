package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository bcr;

    @Override
    public BinaryContentResponse create(BinaryContentCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("request is null.");

        BinaryContent bc = new BinaryContent(request.fileName(), request.contentType(), request.data());
        bcr.save(bc);

        return BinaryContentResponse.from(bc);
    }

    @Override
    public BinaryContentResponse findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return BinaryContentResponse.from(
                bcr.findById(id).orElseThrow(() -> new NoSuchElementException("binaryContent not found"))
        );
    }

    @Override
    public List<BinaryContentResponse> findAllByIdIn(List<UUID> idList) {
        if (idList == null) throw new IllegalArgumentException("idList is null.");

        return idList.stream()
                .filter(Objects::nonNull)
                .map(id -> bcr.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("not found: " + id)))
                .map(BinaryContentResponse::from)
                .toList();
    }

    @Override
    public boolean delete(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        bcr.findById(id).orElseThrow(() -> new NoSuchElementException("binaryContent not found"));
        bcr.delete(id);
        return true;
    }
}
