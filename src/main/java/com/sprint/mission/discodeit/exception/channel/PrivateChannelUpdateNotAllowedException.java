package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class PrivateChannelUpdateNotAllowedException extends ChannelException {

  public PrivateChannelUpdateNotAllowedException() {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED);
  }

  public static PrivateChannelUpdateNotAllowedException withChannelId(UUID channelId) {
    PrivateChannelUpdateNotAllowedException ex = new PrivateChannelUpdateNotAllowedException();
    ex.addDetail("channelId", channelId);
    return ex;
  }
}
