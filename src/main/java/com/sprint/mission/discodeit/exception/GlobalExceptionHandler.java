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
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
    log.warn("IllegalArgumentException : {}", e.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e, 400));
  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    int status = e.getErrorCode().getStatus();

    if (status >= 500) {
      log.error("{}: message={}, details={}",
          e.getClass().getSimpleName(),
          e.getMessage(),
          e.getDetails(),
          e);
    } else {
      log.warn("{}: message={}, details={}",
          e.getClass().getSimpleName(),
          e.getMessage(),
          e.getDetails());
    }

    return ResponseEntity
        .status(HttpStatus.valueOf(status))
        .body(new ErrorResponse(e, status)
        );
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAll(Exception e) {
    log.error("Internal Server Error", e);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(e, 500));
  }
}
