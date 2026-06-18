package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelNameAlreadyExistsException extends ChannelException {

  public ChannelNameAlreadyExistsException() {
    super(ErrorCode.CHANNEL_NAME_DUPLICATED);
  }

  public static ChannelNameAlreadyExistsException withChannelName(String channelName) {
    ChannelNameAlreadyExistsException ex = new ChannelNameAlreadyExistsException();
    ex.addDetail("channelName", channelName);
    return ex;
  }
}
