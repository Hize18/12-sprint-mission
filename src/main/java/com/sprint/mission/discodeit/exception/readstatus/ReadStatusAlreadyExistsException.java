package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ReadStatusAlreadyExistsException extends ReadStatusException {

  public ReadStatusAlreadyExistsException() {
    super(ErrorCode.READ_STATUS_DUPLICATED);
  }

  public static ReadStatusAlreadyExistsException withUserIdAndChannelId(
      UUID userId,
      UUID channelId
  ) {
    ReadStatusAlreadyExistsException ex = new ReadStatusAlreadyExistsException();
    ex.addDetail("userId", userId);
    ex.addDetail("channelId", channelId);
    return ex;
  }
}
