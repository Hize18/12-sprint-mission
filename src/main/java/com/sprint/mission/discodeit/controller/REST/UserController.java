package com.sprint.mission.discodeit.controller.REST;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @RequestMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      method = RequestMethod.POST
  )
  public ResponseEntity<User> createWithImage(
      @RequestPart("userCreateRequest") UserCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    BinaryContentCreateRequest profileImageRequest = toBinaryContentCreateRequest(profile);

    UserCreateRequest createRequest = new UserCreateRequest(
        request.username(),
        request.email(),
        request.password(),
        profileImageRequest
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(createRequest));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @RequestMapping(
      value = "/{userId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      method = RequestMethod.PATCH
  )
  public ResponseEntity<User> updateUser(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    BinaryContentCreateRequest profileImageRequest = toBinaryContentCreateRequest(
        profile);

    UserUpdateRequest updateRequest = new UserUpdateRequest(
        request.newUsername(),
        request.newEmail(),
        request.newPassword(),
        profileImageRequest
    );

    userService.update(userId, updateRequest);
    return ResponseEntity.ok(userService.findById(userId));
  }

  @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteUser(
      @PathVariable UUID userId
  ) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }
  
  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.PATCH)
  public ResponseEntity<UserStatus> userStatusUpdateByUserId(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    return ResponseEntity.ok(userStatusService.updateByUserId(userId, request));
  }

  @RequestMapping(value = "/status/findAll", method = RequestMethod.GET)
  public ResponseEntity<List<UserStatus>> findUserStatusAll() {
    return ResponseEntity.status(HttpStatus.OK).body(userStatusService.findAll());
  }

  @RequestMapping(value = "/{userId}/userStatus", method = RequestMethod.GET)
  public ResponseEntity<UserStatus> checkUserStatus(
      @PathVariable UUID userId
  ) {
    return ResponseEntity.ok(userStatusService.findByUserId(userId));
  }

  private BinaryContentCreateRequest toBinaryContentCreateRequest(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return null;
    }

    try {
      return new BinaryContentCreateRequest(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException("file convert error", e);
    }
  }
}

