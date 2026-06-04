package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.nio.file.Path;

public class FileStorageException extends FileException {

  public FileStorageException() {
    super(ErrorCode.FILE_STORAGE_ERROR);
  }

  public static FileStorageException initError(Path path) {
    FileStorageException ex = new FileStorageException();
    ex.addDetail("rootPath", path);
    return ex;
  }
}
