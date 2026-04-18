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
    private final MessageRepository mr;
    private final ChannelRepository cr;
    private final UserRepository ur;
    private final BinaryContentRepository bcr;

    @Override
    public MessageResponse create(MessageCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("message is null.");

        cr.findById(request.channelId()).orElseThrow(() -> new NoSuchElementException("channel not found."));
        ur.findById(request.userId()).orElseThrow(() -> new NoSuchElementException("user not found."));

        List<UUID> attachmentIds = createAttachments(request.attachmentList());

        Message message = new Message(
                request.channelId(),
                request.userId(),
                request.content(),
                attachmentIds
        );

        return MessageResponse.from(mr.save(message));
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
                    attachmentRequest.data()
            );

            BinaryContent savedBinaryContent = bcr.save(bc);
            attachmentIds.add(savedBinaryContent.getId());
        }

        return attachmentIds;
    }

    @Override
    public MessageResponse findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return MessageResponse.from(
                mr.findById(id).orElseThrow(() -> new NoSuchElementException("message not found."))
        );
    }

    private Message findEntityById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return mr.findById(id).orElseThrow(() -> new NoSuchElementException("message not found."));

    }

    @Override
    public List<MessageResponse> findByUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        return mr.findByUserId(userId).stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        return mr.findByChannelId(channelId).stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Override
    public List<MessageResponse> findAll() {
        return mr.findAll().stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Override
    public boolean update(UUID userId, MessageUpdateRequest request) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (request == null) throw new IllegalArgumentException("messageRequest is null.");

        Message message = findEntityById(request.messageId());

        if (!Objects.equals(message.getUserId(), userId)) return false;

        Message tempMessage = Message.copyOf(message);

        List<UUID> attachmentIds;

        if (request.attachmentList().isEmpty()) {
            attachmentIds = tempMessage.getAttachmentIds();
        } else {
            List<UUID> oldAttachmentIds = List.copyOf(tempMessage.getAttachmentIds());
            attachmentIds = createAttachments(request.attachmentList());
            oldAttachmentIds.forEach(bcr::delete);
        }

        tempMessage.update(request.content(), attachmentIds);
        mr.save(tempMessage);
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID messageId) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        Message message = findEntityById(messageId);

        if (!Objects.equals(message.getUserId(), userId)) return false;

        if (!message.getAttachmentIds().isEmpty()) message.getAttachmentIds().forEach(bcr::delete);
        mr.delete(messageId);
        return true;
    }
}
