package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {BinaryContentMapper.class}
)
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  User toEntity(UserCreateRequest request, BinaryContent profile);

  @Mapping(target = "online", source = "status.online")
  UserDto toDto(User user);
}
