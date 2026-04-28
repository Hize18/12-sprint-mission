package com.sprint.mission.discodeit.controller.REST;


import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Channel", description = "Channel API")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @RequestMapping(value = "/public", method = RequestMethod.POST)
  public ResponseEntity<Channel> createPublicChannel(
      @RequestBody PublicChannelCreateRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.create(request));
  }

  @RequestMapping(value = "/private", method = RequestMethod.POST)
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.create(request));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ChannelDto>> findAllByUserId(
      @RequestParam UUID userId
  ) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
  public ResponseEntity<Channel> updatePublicChannel(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest request
  ) {
    return ResponseEntity.ok(channelService.update(channelId, request));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteChannel(
      @PathVariable UUID channelId
  ) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }
}
