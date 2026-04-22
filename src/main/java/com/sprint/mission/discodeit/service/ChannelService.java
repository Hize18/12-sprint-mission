package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    boolean isUniqueName(String name);

    ChannelResponse createPublicChannel(PublicChannelCreateRequest request);

    ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request);

    ChannelResponse findById(UUID id);

    ChannelResponse findByName(String name);

    List<ChannelResponse> findAll();

    List<ChannelResponse> findAllByUserId(UUID userId);

    boolean update(UUID userId, ChannelUpdateRequest request);

    boolean delete(UUID userId, UUID channelId);
}