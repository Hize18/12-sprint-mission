package com.sprint.mission.discodeit.controller.rest;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Channel", description = "Channel API")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/public")
  public ResponseEntity<ChannelDto> createPublicChannel(
      @RequestBody PublicChannelCreateRequest request
  ) {
    log.debug("public 채널 생성 API 요청: name={}", request.name());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.create(request));
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request
  ) {
    log.debug("private 채널 생성 API 요청: participantCount={}", request.participantIds().size());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.create(request));
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAllByUserId(
      @RequestParam UUID userId
  ) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId));
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updatePublicChannel(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest request
  ) {
    log.debug("채널 업데이트 API 요청: channelId={}", channelId);

    return ResponseEntity.ok(channelService.update(channelId, request));
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable UUID channelId
  ) {
    log.debug("채널 삭제 API 요청: channelId={}", channelId);

    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }
}
