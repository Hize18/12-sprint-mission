package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel create(PublicChannelCreateRequest request);

  Channel create(PrivateChannelCreateRequest request);

  ChannelDto findById(UUID channelId);

  List<ChannelDto> findAll();

  List<ChannelDto> findAllByUserId(UUID userId);

  Channel update(UUID channelId, ChannelUpdateRequest request);

  void delete(UUID channelId);
}