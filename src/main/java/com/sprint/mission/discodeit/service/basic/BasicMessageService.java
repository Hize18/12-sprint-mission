package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public Message create(MessageCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("message is null.");
    }

    if (!channelRepository.existsById(request.channelId())) {
      throw new NotFoundException("channel not found.");
    }

    if (!userRepository.existsById(request.authorId())) {
      throw new NotFoundException("user not found.");
    }

    List<UUID> attachmentIds = createAttachments(request.attachmentList());

    Message message = new Message(
        request.channelId(),
        request.authorId(),
        request.content(),
        attachmentIds
    );

    return messageRepository.save(message);
  }

  @Override
  public Message findById(UUID messageId) {
    if (messageId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("message not found."));
  }

  private Message findEntityById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return messageRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("message not found."));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

    return messageRepository.findByChannelId(channelId);
  }

  @Override
  public List<Message> findByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    return messageRepository.findByUserId(userId);
  }

  @Override
  public List<Message> findAll() {
    return messageRepository.findAll();
  }

  @Override
  public Message update(UUID messageId, MessageUpdateRequest request) {
    if (messageId == null) {
      throw new IllegalArgumentException("messageId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("messageRequest is null.");
    }

    Message message = findEntityById(messageId);

    Message tempMessage = Message.copyOf(message);

    List<UUID> attachmentIds;

    if (request.attachmentList().isEmpty()) {
      attachmentIds = tempMessage.getAttachmentIds();
    } else {
      List<UUID> oldAttachmentIds = List.copyOf(tempMessage.getAttachmentIds());
      attachmentIds = createAttachments(request.attachmentList());
      oldAttachmentIds.forEach(binaryContentRepository::delete);
    }

    tempMessage.update(request.newContent(), attachmentIds);
    return messageRepository.save(tempMessage);
  }

  @Override
  public void delete(UUID messageId) {
    if (messageId == null) {
      throw new IllegalArgumentException("messageId is null.");
    }

    Message message = findEntityById(messageId);

    if (!message.getAttachmentIds().isEmpty()) {
      message.getAttachmentIds().forEach(binaryContentRepository::delete);
    }
    messageRepository.delete(messageId);
  }

  private List<UUID> createAttachments(List<BinaryContentCreateRequest> attachmentRequests) {
    if (attachmentRequests.isEmpty()) {
      return List.of();
    }

    List<UUID> attachmentIds = new ArrayList<>();

    for (BinaryContentCreateRequest attachmentRequest : attachmentRequests) {
      if (attachmentRequest == null) {
        throw new IllegalArgumentException("attachmentRequest is null.");
      }
      BinaryContent bc = new BinaryContent(
          attachmentRequest.fileName(),
          attachmentRequest.contentType(),
          attachmentRequest.bytes()
      );

      BinaryContent savedBinaryContent = binaryContentRepository.save(bc);
      attachmentIds.add(savedBinaryContent.getId());
    }

    return attachmentIds;
  }
}
