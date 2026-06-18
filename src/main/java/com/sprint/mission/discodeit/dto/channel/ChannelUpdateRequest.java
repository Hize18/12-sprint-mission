package com.sprint.mission.discodeit.dto.channel;

public record ChannelUpdateRequest(
    String newName,
    String newDescription
) {
//  채널업데이트는 프론트에서 어떻게 주는지 아직 확인불가. 일단 compact로 유지

  public ChannelUpdateRequest {
    if (newName == null) {
      throw new IllegalArgumentException("newName is null.");
    }

    if (newDescription == null) {
      throw new IllegalArgumentException("newDescription is null.");
    }
  }
}