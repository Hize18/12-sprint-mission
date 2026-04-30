package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
    log.warn("IllegalArgumentException : {}", e.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)//400
        .body(e.getMessage());
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<String> handleUnauthorized(UnauthorizedException e) {
    log.warn("UnauthorizedException: {}", e.getMessage());

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)//401
        .body(e.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFound(NotFoundException e) {
    log.warn("NotFoundException: {}", e.getMessage());

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)//404
        .body(e.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> handleIllegalState(IllegalStateException e) {
    log.warn("IllegalStateException: {}", e.getMessage());

    return ResponseEntity
        .status(HttpStatus.CONFLICT)//409
        .body(e.getMessage());
  }

  @ExceptionHandler(DuplicateException.class)
  public ResponseEntity<String> handleDuplicate(DuplicateException e) {
    log.warn("DuplicateException: {}", e.getMessage());

    return ResponseEntity
        .status(HttpStatus.CONFLICT)//409
        .body(e.getMessage());
  }

  @ExceptionHandler(FileProcessingException.class)
  public ResponseEntity<String> handleFileProcessing(FileProcessingException e) {
    log.error("FileProcessingException", e);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)//500
        .body("FileProcessing Error");
  }

  @ExceptionHandler(FileStorageException.class)
  public ResponseEntity<String> handleFileStorage(FileStorageException e) {
    log.error("FileStorageException", e);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)//500
        .body("FileStorage Error");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleAll(Exception e) {
    log.error("Internal Server Error", e);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)//500
        .body("Internal Server Error");
  }
}
