package com.sprint.mission.discodeit.controller.REST;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

//    @RequestMapping(value = "/", method = RequestMethod.POST)
//    public ResponseEntity<MessageResponse> sendMessage(
//            @RequestBody MessageCreateRequest request
//    ){
//        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(request));
//    }

  @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponse> createWithAttachments(
      @RequestPart("request") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) throws IOException {

    List<BinaryContentCreateRequest> attachmentList = null;

    if (attachments != null && !attachments.isEmpty()) {
      attachmentList = attachments.stream()
          .filter(file -> file != null && !file.isEmpty())
          .map(file -> {
            try {
              return new BinaryContentCreateRequest(
                  file.getOriginalFilename(),
                  file.getContentType(),
                  file.getBytes()
              );
            } catch (IOException e) {
              throw new RuntimeException("file convert error", e);
            }
          })
          .toList();
    }

    MessageCreateRequest createRequest = new MessageCreateRequest(
        request.channelId(),
        request.userId(),
        request.content(),
        attachmentList
    );

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(messageService.create(createRequest));
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
  public ResponseEntity<MessageResponse> updateMessage(
      @RequestBody MessageUpdateRequest request,
      @PathVariable UUID messageId,
      HttpSession session
  ) {
    UserResponse userResponse = (UserResponse) session.getAttribute("loginUser");
    if (userResponse == null) {
      throw new IllegalStateException("not login yet");
    }
    messageService.update(userResponse.id(), messageId, request);
    return ResponseEntity.status(HttpStatus.OK).body(messageService.findById(messageId));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<List<MessageResponse>> deleteMessage(
      @PathVariable UUID id,
      HttpSession session
  ) {
    UserResponse userResponse = (UserResponse) session.getAttribute("loginUser");
    if (userResponse == null) {
      throw new IllegalStateException("not login yet");
    }
    messageService.delete(userResponse.id(), id);
    return ResponseEntity.status(HttpStatus.OK).body(messageService.findAll());
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
  public ResponseEntity<List<MessageResponse>> findAllByChannelId(
      @PathVariable UUID channelId
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(messageService.findAllByChannelId(channelId));
  }
}
