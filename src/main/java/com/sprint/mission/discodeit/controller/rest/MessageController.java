package com.sprint.mission.discodeit.controller.rest;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.FileProcessingException;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @RequestMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      method = RequestMethod.POST
  )
  public ResponseEntity<Message> createWithAttachments(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    MessageCreateRequest createRequest = new MessageCreateRequest(
        request.channelId(),
        request.authorId(),
        request.content(),
        toBinaryContentCreateRequests(attachments)
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(createRequest));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Message>> findAllByChannelId(
      @RequestParam UUID channelId
  ) {
    return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
  }

  @RequestMapping(
      value = "/{messageId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      method = RequestMethod.PATCH
  )
  public ResponseEntity<Message> updateMessage(
      @RequestPart("messageUpdateRequest") MessageUpdateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments,
      @PathVariable UUID messageId
  ) {
    MessageUpdateRequest updateRequest = new MessageUpdateRequest(
        request.newContent(),
        toBinaryContentCreateRequests(attachments)
    );

    messageService.update(messageId, updateRequest);

    return ResponseEntity.ok(messageService.findById(messageId));
  }

  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteMessage(
      @PathVariable UUID messageId
  ) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  private List<BinaryContentCreateRequest> toBinaryContentCreateRequests(
      List<MultipartFile> files
  ) {
    if (files == null || files.isEmpty()) {
      return null;
    }

    List<BinaryContentCreateRequest> result = files.stream()
        .filter(file -> file != null && !file.isEmpty())
        .map(this::toBinaryContentCreateRequest)
        .toList();

    return result.isEmpty() ? null : result;
  }

  private BinaryContentCreateRequest toBinaryContentCreateRequest(MultipartFile file) {
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
