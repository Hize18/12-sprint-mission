package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.FileStorageException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "12-sprint-mission.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(
      @Value("${12-sprint-mission.storage.local.root-path:local_storage}") String directory
  ) {
    this.root = Path.of(directory);
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to initialize storage.", e);
    }
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path path = resolvePath(binaryContentId);

    try {
      Files.write(path, bytes);
    } catch (IOException e) {
      throw new FileStorageException("Failed to store binary content.");
    }

    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    Path path = resolvePath(binaryContentId);

    if (Files.notExists(path)) {
      throw new NotFoundException("Binary content file not found.");
    }

    try {
      return Files.newInputStream(path);
    } catch (IOException e) {
      throw new FileStorageException("Failed to read binary content.");
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    InputStream inputStream = get(binaryContentDto.id());

    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + binaryContentDto.fileName() + "\""
        )
        .contentType(
            MediaType.parseMediaType(binaryContentDto.contentType())
        )
        .contentLength(binaryContentDto.size())
        .body(resource);
  }

  public Path resolvePath(UUID binaryContentId) {
    return root.resolve(binaryContentId.toString());
  }
}
