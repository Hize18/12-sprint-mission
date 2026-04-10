package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository mr;
    private final ChannelRepository cr;
    private final UserRepository ur;

    @Override
    public Message save(Message message) {
        if (message == null) throw new IllegalArgumentException("message is null.");

        if (message.getChannelId() == null) throw new IllegalArgumentException("channelId is null.");
        if (message.getUserId() == null) throw new IllegalArgumentException("userId is null.");

        if (message.getContent() == null) throw new IllegalArgumentException("content is null.");
        if (message.getContent().isBlank()) throw new IllegalArgumentException("content is blank.");

        cr.findById(message.getChannelId()).orElseThrow(() -> new NoSuchElementException("channel not found."));
        ur.findById(message.getUserId()).orElseThrow(() -> new NoSuchElementException("user not found."));


        return mr.save(message);
    }

    @Override
    public Message findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return mr.findById(id).orElseThrow(() -> new NoSuchElementException("message not found."));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");

        return mr.findByChannelId(channelId);
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        return mr.findByUserId(userId);
    }

    @Override
    public List<Message> findAll() {
        return mr.findAll();
    }

    @Override
    public boolean update(UUID userId, UUID messageId, String content) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (content == null) throw new IllegalArgumentException("content is null.");

        Message message = findById(messageId);

        if (!Objects.equals(message.getUserId(), userId)) return false;

        Message tempMessage = Message.copyOf(message);

        tempMessage.update(content);
        mr.save(tempMessage);
        return true;
    }

    @Override
    public boolean delete(UUID userId, UUID messageId) {
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        Message message = findById(messageId);

        if (!Objects.equals(message.getUserId(), userId)) return false;

        mr.delete(messageId);
        return true;
    }
}
