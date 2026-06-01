package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  @Mapping(target = "size", expression = "java((long) request.bytes().length)")
  BinaryContent toEntity(BinaryContentCreateRequest request);

  BinaryContentDto toDto(BinaryContent binaryContent);
}
