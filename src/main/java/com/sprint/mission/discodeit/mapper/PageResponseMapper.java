package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.page.PageResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

  default <T> PageResponse<T> fromSlice(List<T> content, Object nextCursor, Slice<?> slice) {
    return new PageResponse<>(
        content,
        nextCursor,
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }

  default <T> PageResponse<T> fromPage(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}
