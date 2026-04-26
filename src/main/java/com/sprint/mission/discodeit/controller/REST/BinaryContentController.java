package com.sprint.mission.discodeit.controller.REST;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "/find", method = RequestMethod.GET)
  public ResponseEntity<BinaryContentResponse> findById(
      @Parameter UUID binaryContentId
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(binaryContentService.findById(binaryContentId));
  }

  //    단일 및 심화용 엔드포인트
  @RequestMapping(value = "/findAll", method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContentResponse>> findAllByList(
      @RequestParam List<UUID> binarybinaryContentList
  ) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(binaryContentService.findAllByIdIn(binarybinaryContentList));
  }
}
