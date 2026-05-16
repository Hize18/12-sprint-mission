package com.sprint.mission.discodeit.dto.page;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    int number,
    int size,
    boolean hasNext,
    Long totalElements
) {

  public PageResponse {
    if (content == null) {
      throw new IllegalArgumentException("content is null.");
    }
    if (number < 0) {
      throw new IllegalArgumentException("number is negative.");
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
