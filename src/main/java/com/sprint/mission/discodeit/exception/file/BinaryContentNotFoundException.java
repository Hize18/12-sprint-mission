package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class BinaryContentNotFoundException extends FileException {

  public BinaryContentNotFoundException() {
    super(ErrorCode.BINARY_CONTENT_NOT_FOUND);
  }

  public static BinaryContentNotFoundException withBinaryContentId(UUID binaryContentId) {
    BinaryContentNotFoundException ex = new BinaryContentNotFoundException();
    ex.addDetail("binaryContentId", binaryContentId);
    return ex;
  }
}
