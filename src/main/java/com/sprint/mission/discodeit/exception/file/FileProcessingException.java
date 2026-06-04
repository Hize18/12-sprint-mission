package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileProcessingException extends FileException {

  public FileProcessingException() {
    super(ErrorCode.FILE_PROCESSING_ERROR);
  }
}
