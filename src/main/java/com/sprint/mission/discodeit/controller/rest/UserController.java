package com.sprint.mission.discodeit.controller.rest;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.FileProcessingException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> createWithImage(
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

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.findAllWithFetch());
  }

  @PatchMapping(
      value = "/{userId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<UserDto> updateUser(
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
    return ResponseEntity.ok(userService.findDetailById(userId));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(
      @PathVariable UUID userId
  ) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> userStatusUpdateByUserId(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    return ResponseEntity.ok(userStatusService.updateByUserId(userId, request));
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
      throw new FileProcessingException("file convert error");
    }
  }
}

