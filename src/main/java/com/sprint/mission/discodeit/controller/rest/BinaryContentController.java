package com.sprint.mission.discodeit.controller.rest;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContentDto> find(
      @PathVariable UUID binaryContentId
  ) {
    return ResponseEntity.ok(binaryContentService.findById(binaryContentId));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam List<UUID> binaryContentIds
  ) {
    return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
  }

  @RequestMapping(value = "/{binaryContentId}/download", method = RequestMethod.GET)
  public ResponseEntity<Resource> download(
      @PathVariable UUID binaryContentId
  ) {
    BinaryContentDto binaryContentDto = binaryContentService.findById(binaryContentId);
    return binaryContentStorage.download(binaryContentDto);
  }
}
