package com.sprint.mission.discodeit.dto.page;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    Object nextCursor,
    int size,
    boolean hasNext,
    Long totalElements
) {

  public PageResponse {
    if (content == null) {
      throw new IllegalArgumentException("content is null.");
    }
    if (size < 0) {
      throw new IllegalArgumentException("size is negative.");
    }
    if (totalElements != null && totalElements < 0) {
      throw new IllegalArgumentException("totalElements is negative.");
    }

    content = List.copyOf(content);
  }
}
