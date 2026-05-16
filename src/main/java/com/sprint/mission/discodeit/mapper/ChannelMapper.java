package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class},
    imports = {ChannelType.class}
)
public interface ChannelMapper {

  @Mapping(target = "type",
      expression = "java(ChannelType.PUBLIC)")
  Channel toEntity(PublicChannelCreateRequest request);

  ChannelDto toDto(Channel channel, Instant lastMessageAt, List<UserDto> participants);
}