package com.sprint.mission.discodeit.controller.rest;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  public final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatusDto> create(
      @RequestBody @Valid ReadStatusCreateRequest request
  ) {
    log.debug("읽음 상태 생성 API 요청: userId={}, channelId={}",
        request.userId(),
        request.channelId()
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusService.create(request));
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
      @RequestParam UUID userId
  ) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }

  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(
      @PathVariable UUID readStatusId,
      @RequestBody @Valid ReadStatusUpdateRequest request
  ) {
    return ResponseEntity.ok(readStatusService.update(readStatusId, request));
  }
}
