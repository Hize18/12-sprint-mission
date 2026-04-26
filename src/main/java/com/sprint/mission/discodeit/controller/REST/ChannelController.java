package com.sprint.mission.discodeit.controller.REST;


import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @RequestMapping(value = "/create/public", method = RequestMethod.POST)
  public ResponseEntity<ChannelResponse> createPublicChannel(
      @RequestBody PublicChannelCreateRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPublicChannel(request));
  }

  @RequestMapping(value = "/create/private", method = RequestMethod.POST)
  public ResponseEntity<ChannelResponse> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPrivateChannel(request));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
  public ResponseEntity<ChannelResponse> updatePublicChannel(
      @RequestBody ChannelUpdateRequest request,
      @PathVariable UUID channelId,
      HttpSession session
  ) {
    UserResponse userResponse = (UserResponse) session.getAttribute("loginUser");
    if (userResponse == null) {
      throw new IllegalArgumentException("not login yet.");
    }
    channelService.update(userResponse.id(), channelId, request);
    return ResponseEntity.status(HttpStatus.OK).body(channelService.findById(channelId));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<List<ChannelResponse>> deleteChannel(
      @PathVariable UUID id,
      HttpSession session
  ) {
    UserResponse userResponse = (UserResponse) session.getAttribute("loginUser");
    if (userResponse == null) {
      throw new IllegalArgumentException("not login yet.");
    }
    channelService.delete(userResponse.id(), id);
    return ResponseEntity.status(HttpStatus.OK).body(channelService.findAll());
  }

  @RequestMapping(value = "/find/{userId}", method = RequestMethod.GET)
  public ResponseEntity<List<ChannelResponse>> findChannelByUserId(
      @PathVariable UUID userId
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(channelService.findAllByUserId(userId));
  }
}
