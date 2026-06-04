package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException() {
    super(ErrorCode.CHANNEL_NOT_FOUND);
  }

  public static ChannelNotFoundException withChannelName(String channelName) {
    ChannelNotFoundException ex = new ChannelNotFoundException();
    ex.addDetail("channelName", channelName);
    return ex;
  }

  public static ChannelNotFoundException withChannelId(UUID channelId) {
    ChannelNotFoundException ex = new ChannelNotFoundException();
    ex.addDetail("channelId", channelId);
    return ex;
  }
}
