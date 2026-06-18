package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class BinaryContentFileNotFoundException extends FileException {

  public BinaryContentFileNotFoundException() {
    super(ErrorCode.BINARY_CONTENT_FILE_NOT_FOUND);
  }

  public static BinaryContentFileNotFoundException withBinaryContentId(UUID binaryContentId) {
    BinaryContentFileNotFoundException ex = new BinaryContentFileNotFoundException();
    ex.addDetail("binaryContentId", binaryContentId);
    return ex;
  }
}
