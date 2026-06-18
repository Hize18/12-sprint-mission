package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.nio.file.Path;

public class FileStorageException extends FileException {

  public FileStorageException() {
    super(ErrorCode.FILE_STORAGE_ERROR);
  }

  public FileStorageException(Throwable cause) {
    super(ErrorCode.FILE_STORAGE_ERROR, cause);
  }

  public static FileStorageException initError(Path path, Throwable cause) {
    FileStorageException ex = new FileStorageException(cause);
    ex.addDetail("rootPath", path);
    return ex;
  }
}
