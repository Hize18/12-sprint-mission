package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.rest.MessageController;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MessageController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMetamodelMappingContext;

  @Test
  @DisplayName("create_message_success")
  void create_message_success() throws Exception {
    UserDto userDto = new UserDto(
        UUID.randomUUID(),
        "username",
        "email@email.com",
        null,
        false
    );

    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(
        UUID.randomUUID(),
        userDto.id(),
        "content"
    );

    MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(messageCreateRequest)
    );

    MockMultipartFile attachments = new MockMultipartFile(
        "attachments",
        "attachments.png",
        "image/png",
        "attachments".getBytes()
    );

    BinaryContentDto attachmentDto = new BinaryContentDto(
        UUID.randomUUID(),
        attachments.getOriginalFilename(),
        attachments.getSize(),
        attachments.getContentType()
    );

    MessageDto messageDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        messageCreateRequest.content(),
        messageCreateRequest.channelId(),
        userDto,
        List.of(attachmentDto)
    );

    given(messageService.create(eq(messageCreateRequest), any())).willReturn(messageDto);

    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequestPart)
            .file(attachments))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(messageDto.id().toString()))
        .andExpect(jsonPath("$.content").value(messageDto.content()))
        .andExpect(jsonPath("$.channelId").value(messageDto.channelId().toString()))
        .andExpect(jsonPath("$.author.id").value(userDto.id().toString()))
        .andExpect(jsonPath("$.author.username").value(userDto.username()))
        .andExpect(jsonPath("$.attachments[0].id").value(attachmentDto.id().toString()))
        .andExpect(jsonPath("$.attachments[0].fileName").value(attachmentDto.fileName()))
        .andExpect(jsonPath("$.attachments[0].size").value(attachmentDto.size()))
        .andExpect(jsonPath("$.attachments[0].contentType").value(attachmentDto.contentType()));
  }

  @Test
  @DisplayName("create_message_failed")
  void create_message_failed() throws Exception {
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(
        UUID.randomUUID(),
        null,
        "content"
    );

    MockMultipartFile messageCreateRequestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(messageCreateRequest)
    );

    mockMvc.perform(multipart("/api/messages")
            .file(messageCreateRequestPart))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
  }

  @Test
  @DisplayName("find_all_by_channel_id")
  void find_all_by_channel_id() throws Exception {
    UUID channelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 10);

    UserDto userDto = new UserDto(
        UUID.randomUUID(),
        "username",
        "email@email.com",
        null,
        false
    );

    MessageDto messageDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "content",
        channelId,
        userDto,
        List.of()
    );

    PageResponse<MessageDto> response = new PageResponse<>(
        List.of(messageDto),
        null,
        pageable.getPageSize(),
        false,
        null
    );

    given(messageService.findAllByChannelId(eq(channelId), eq(null), any(Pageable.class)))
        .willReturn(response);

    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("size", String.valueOf(pageable.getPageSize())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.size()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(messageDto.id().toString()))
        .andExpect(jsonPath("$.content[0].content").value(messageDto.content()))
        .andExpect(jsonPath("$.content[0].channelId").value(channelId.toString()))
        .andExpect(jsonPath("$.content[0].author.id").value(userDto.id().toString()))
        .andExpect(jsonPath("$.content[0].author.username").value(userDto.username()))
        .andExpect(jsonPath("$.content[0].attachments.size()").value(0))
        .andExpect(jsonPath("$.nextCursor").doesNotExist())
        .andExpect(jsonPath("$.size").value(pageable.getPageSize()))
        .andExpect(jsonPath("$.hasNext").value(false))
        .andExpect(jsonPath("$.totalElements").doesNotExist());
  }

  @Test
  @DisplayName("update_message_success")
  void update_message_success() throws Exception {
    UserDto userDto = new UserDto(
        UUID.randomUUID(),
        "username",
        "email@email.com",
        null,
        false
    );

    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(
        "newContent"
    );

    MockMultipartFile messageUpdateRequestPart = new MockMultipartFile(
        "messageUpdateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(messageUpdateRequest)
    );

    MessageDto messageDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        messageUpdateRequest.newContent(),
        UUID.randomUUID(),
        userDto,
        List.of()
    );

    given(messageService.update(eq(messageDto.id()), eq(messageUpdateRequest), any()))
        .willReturn(messageDto);

    mockMvc.perform(multipart("/api/messages/{messageId}", messageDto.id())
            .file(messageUpdateRequestPart)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageDto.id().toString()))
        .andExpect(jsonPath("$.content").value(messageDto.content()))
        .andExpect(jsonPath("$.channelId").value(messageDto.channelId().toString()))
        .andExpect(jsonPath("$.author.id").value(messageDto.author().id().toString()))
        .andExpect(jsonPath("$.author.username").value(userDto.username()));
  }

  @Test
  @DisplayName("update_message_failed")
  void update_message_failed() throws Exception {
    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(
        null
    );

    MockMultipartFile messageUpdateRequestPart = new MockMultipartFile(
        "messageUpdateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(messageUpdateRequest)
    );

    mockMvc.perform(multipart("/api/messages/{messageId}", UUID.randomUUID())
            .file(messageUpdateRequestPart)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
  }

  @Test
  @DisplayName("delete_message_success")
  void delete_message_success() throws Exception {
    UUID messageId = UUID.randomUUID();

    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());

    verify(messageService, times(1)).delete(eq(messageId));
  }

  @Test
  @DisplayName("delete_message_failed")
  void delete_message_failed() throws Exception {
    UUID messageId = UUID.randomUUID();

    willThrow(MessageNotFoundException.withMessageId(messageId))
        .given(messageService)
        .delete(messageId);

    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("MESSAGE_NOT_FOUND"));
  }
}
