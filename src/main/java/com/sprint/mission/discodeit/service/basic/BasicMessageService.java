package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final PageResponseMapper pageResponseMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("message is null.");
    }

    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new NotFoundException("channel not found."));

    User author = userRepository.findDetailById(request.authorId())
        .orElseThrow(() -> new NotFoundException("user not found."));

    List<BinaryContent> attachments = createAttachments(request.attachmentList());

    Message message = messageMapper.toEntity(request, channel, author, attachments);
    return messageMapper.toDto(messageRepository.save(message));
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto findDetailById(UUID messageId) {
    if (messageId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    Message message = messageRepository.findDetailById(messageId)
        .orElseThrow(() -> new NotFoundException("message not found."));
    return messageMapper.toDto(message);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

//    (:cursor is null or m.createdAt < :cursor)로 하나의 쿼리는 에러 발생
    Slice<UUID> messageIdSlice = cursor == null
        ? messageRepository.findIdsByChannelId(channelId, pageable)
        : messageRepository.findIdsByChannelIdAndCreatedAtLessThan(channelId, cursor, pageable);

    Map<UUID, Message> messageMap = messageRepository
        .findAllDetailByIdIn(messageIdSlice.getContent()).stream()
        .collect(Collectors.toMap(Message::getId, message -> message));

    List<MessageDto> content = messageIdSlice.getContent().stream()
        .map(messageId -> messageMapper.toDto(messageMap.get(messageId)))
        .toList();

    Instant nextCursor = null;
    if (messageIdSlice.hasNext() && !content.isEmpty()) {
      nextCursor = content.get(content.size() - 1).createdAt();
    }

    return pageResponseMapper.fromSlice(content, nextCursor, messageIdSlice);
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    if (messageId == null) {
      throw new IllegalArgumentException("messageId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("messageRequest is null.");
    }

    Message message = messageRepository.findDetailById(messageId)
        .orElseThrow(() -> new NotFoundException("message not found."));

    message.setContent(request.newContent());

    if (!request.attachmentList().isEmpty()) {
      List<BinaryContent> attachments = createAttachments(request.attachmentList());
      message.setAttachments(attachments);
    }

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    if (messageId == null) {
      throw new IllegalArgumentException("messageId is null.");
    }

    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("message not found."));

    messageRepository.delete(message);
  }

  private List<BinaryContent> createAttachments(
      List<BinaryContentCreateRequest> attachmentRequests) {
    if (attachmentRequests.isEmpty()) {
      return List.of();
    }

    List<BinaryContent> attachments = new ArrayList<>();

    for (BinaryContentCreateRequest attachmentRequest : attachmentRequests) {
      if (attachmentRequest == null) {
        throw new IllegalArgumentException("attachmentRequest is null.");
      }
      BinaryContent binaryContent = binaryContentRepository.save(
          binaryContentMapper.toEntity(attachmentRequest));
      binaryContentStorage.put(binaryContent.getId(), attachmentRequest.bytes());
      attachments.add(binaryContent);
    }

    return attachments;
  }
}
