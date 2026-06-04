package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException() {
    super(ErrorCode.READ_STATUS_NOT_FOUND);
  }

  public static ReadStatusNotFoundException withReadStatusId(UUID readStatusId) {
    ReadStatusNotFoundException ex = new ReadStatusNotFoundException();
    ex.addDetail("readStatusId", readStatusId);
    return ex;
  }
}
