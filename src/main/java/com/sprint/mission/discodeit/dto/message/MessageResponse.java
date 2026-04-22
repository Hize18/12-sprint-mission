package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID channelId,
        UUID userId,
        List<UUID> attachmentIds,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public MessageResponse {
        attachmentIds = attachmentIds == null ? List.of() : List.copyOf(attachmentIds);
    }

    public static MessageResponse from(Message message){
        if (message == null) throw new IllegalArgumentException("message is null.");

        return new MessageResponse(
                message.getId(),
                message.getChannelId(),
                message.getUserId(),
                message.getAttachmentIds(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault());

    @Override
    public String toString() {
        return "Message [" +
                "channelId = " + channelId +
                ", userId = " + userId +
                ", content = " + content +
                "]\t시간 = " +
                FORMATTER.format(updatedAt)
                + (updatedAt.isAfter(createdAt) ? "(수정됨)" : "");
    }
}
