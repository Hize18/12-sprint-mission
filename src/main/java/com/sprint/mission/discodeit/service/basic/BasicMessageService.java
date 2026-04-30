package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("message is null.");

        if(!channelRepository.existsById(request.channelId())) throw new NoSuchElementException("channel not found.");

        if(!userRepository.existsById(request.userId())) throw new NoSuchElementException("user not found.");

        List<UUID> attachmentIds = createAttachments(request.attachmentList());

        Message message = new Message(
                request.channelId(),
                request.userId(),
                request.content(),
                attachmentIds
        );

        return MessageResponse.from(messageRepository.save(message));
    }

    private List<UUID> createAttachments(List<BinaryContentCreateRequest> attachmentRequests) {
        if (attachmentRequests.isEmpty()) return List.of();

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

    @Override
    public MessageResponse findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return MessageResponse.from(
                messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("message not found."))
        );
    }

    private Message findEntityById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("message not found."));
    }

    @Override
    public List<MessageResponse> findByUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        return messageRepository.findByUserId(userId).stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        return messageRepository.findByChannelId(channelId).stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Override
    public List<MessageResponse> findAll() {
        return messageRepository.findAll().stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Override
    public void update(UUID userId, UUID messageId, MessageUpdateRequest request) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (request == null) throw new IllegalArgumentException("messageRequest is null.");

        Message message = findEntityById(messageId);

        if (!Objects.equals(message.getUserId(), userId)) throw new IllegalStateException("no permission");

        Message tempMessage = Message.copyOf(message);

        List<UUID> attachmentIds;

        if (request.attachmentList().isEmpty()) {
            attachmentIds = tempMessage.getAttachmentIds();
        } else {
            List<UUID> oldAttachmentIds = List.copyOf(tempMessage.getAttachmentIds());
            attachmentIds = createAttachments(request.attachmentList());
            oldAttachmentIds.forEach(binaryContentRepository::delete);
        }

        tempMessage.update(request.content(), attachmentIds);
        messageRepository.save(tempMessage);
    }

    @Override
    public void delete(UUID userId, UUID messageId) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        Message message = findEntityById(messageId);

        if (!Objects.equals(message.getUserId(), userId)) throw new IllegalStateException("no permission");

        if (!message.getAttachmentIds().isEmpty()) message.getAttachmentIds().forEach(binaryContentRepository::delete);
        messageRepository.delete(messageId);
    }
}
