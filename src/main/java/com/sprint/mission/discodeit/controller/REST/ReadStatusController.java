package com.sprint.mission.discodeit.controller.REST;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {

  public final ReadStatusService readStatusService;

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public ResponseEntity<ReadStatusResponse> create(
      @RequestBody ReadStatusCreateRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusService.create(request));
  }

  @RequestMapping(value = "/user/{userId}/channel/{channelId}", method = RequestMethod.PUT)
  public ResponseEntity<ReadStatusResponse> update(
      @PathVariable UUID userId,
      @PathVariable UUID channelId
  ) {
    readStatusService.update(userId, channelId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatusService.findByUserIdAndChannelId(userId, channelId));
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatusResponse>> findAllByUserId(
      @PathVariable UUID userId
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(readStatusService.findAllByUserId(userId));
  }
}
