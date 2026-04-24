package com.sprint.mission.discodeit.controller.REST;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

//    @RequestMapping(value = "/", method = RequestMethod.POST)
//    public ResponseEntity<UserResponse> create(
//            @RequestBody UserCreateRequest user
//    ){
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
//    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> createWithImage(
            @RequestPart("username") String username,
            @RequestPart("email") String email,
            @RequestPart("password") String password,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {
        BinaryContentCreateRequest profileImageRequest = null;

        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageRequest = new BinaryContentCreateRequest(
                    profileImage.getOriginalFilename(),
                    profileImage.getContentType(),
                    profileImage.getBytes()
            );
        }

        UserCreateRequest request = new UserCreateRequest(
                username,
                email,
                password,
                profileImageRequest
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateRequest userUpdateRequest,
            HttpSession session
    ){
        UserResponse userResponse = (UserResponse) session.getAttribute("loginUser");
        if(userResponse == null) throw new IllegalStateException("not login yet");
        userService.update(userResponse.id(), id, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<List<UserResponse>> deleteUser(
            @PathVariable UUID id,
            HttpSession session
    ){
        UserResponse userResponse = (UserResponse) session.getAttribute("loginUser");
        if(userResponse == null) throw new IllegalStateException("not login yet");
        userService.delete(userResponse.id(), id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @RequestMapping(value = "/delete_Force/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<List<UserResponse>> deleteUserForce(
            @PathVariable UUID id
    ){
//        테스트 용
        userService.delete(id, id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @RequestMapping(value = "/status/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<UserStatusResponse> userStatusUpdateByUserId(
            @PathVariable UUID userId
    ){
        userStatusService.updateByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userStatusService.findByUserId(userId));
    }

    @RequestMapping(value = "/status/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserStatusResponse> checkUserStatus(
            @PathVariable UUID userId
    ){
        return ResponseEntity.status(HttpStatus.OK).body(userStatusService.findByUserId(userId));
    }

    @RequestMapping(value = "/status/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserStatusResponse>> findUserStatusAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userStatusService.findAll());
    }
}

