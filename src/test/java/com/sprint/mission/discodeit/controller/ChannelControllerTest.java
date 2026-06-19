package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.rest.ChannelController;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ChannelController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @MockitoBean
  private JpaMetamodelMappingContext jpaMetamodelMappingContext;

  @Test
  @DisplayName("create_public_channel_success")
  void create_public_channel_success() throws Exception {
    PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
        "public_channel",
        "description"
    );

    ChannelDto channelDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        publicChannelCreateRequest.name(),
        publicChannelCreateRequest.description(),
        null,
        null
    );

    given(channelService.create(publicChannelCreateRequest)).willReturn(channelDto);

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(publicChannelCreateRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelDto.id().toString()))
        .andExpect(jsonPath("$.type").value(channelDto.type().name()))
        .andExpect(jsonPath("$.name").value(channelDto.name()))
        .andExpect(jsonPath("$.description").value(channelDto.description()))
        .andExpect(jsonPath("$.lastMessageAt").doesNotExist());
  }

  @Test
  @DisplayName("create_public_channel_failed")
  void create_public_channel_failed() throws Exception {
    PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
        null,
        "description"
    );

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(publicChannelCreateRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
  }

  @Test
  @DisplayName("find_all_by_user_id")
  void find_all_by_user_id() throws Exception {
    UUID userId = UUID.randomUUID();

    ChannelDto channelDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        "public_channel",
        "description",
        null,
        null
    );

    given(channelService.findAllByUserId(userId)).willReturn(List.of(channelDto));

    mockMvc.perform(get("/api/channels")
            .param("userId", String.valueOf(userId)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(channelDto.id().toString()))
        .andExpect(jsonPath("$.[0].type").value(channelDto.type().name()))
        .andExpect(jsonPath("$.[0].name").value(channelDto.name()))
        .andExpect(jsonPath("$.[0].description").value(channelDto.description()))
        .andExpect(jsonPath("$.[0].lastMessageAt").doesNotExist());
  }

  @Test
  @DisplayName("update_public_channel_success")
  void update_public_channel_success() throws Exception {
    UUID channelId = UUID.randomUUID();

    ChannelUpdateRequest channelUpdateRequest = new ChannelUpdateRequest(
        "newName",
        "newDescription"
    );

    ChannelDto channelDto = new ChannelDto(
        channelId,
        ChannelType.PUBLIC,
        channelUpdateRequest.newName(),
        channelUpdateRequest.newDescription(),
        null,
        null
    );

    given(channelService.update(channelId, channelUpdateRequest)).willReturn(channelDto);

    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelUpdateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelDto.id().toString()))
        .andExpect(jsonPath("$.type").value(channelDto.type().name()))
        .andExpect(jsonPath("$.name").value(channelDto.name()))
        .andExpect(jsonPath("$.description").value(channelDto.description()))
        .andExpect(jsonPath("$.lastMessageAt").doesNotExist());
  }

  @Test
  @DisplayName("update_public_channel_failed")
  void update_public_channel_failed() throws Exception {
    UUID channelId = UUID.randomUUID();

    ChannelUpdateRequest channelUpdateRequest = new ChannelUpdateRequest(
        "newName",
        "newDescription"
    );

    given(channelService.update(channelId, channelUpdateRequest))
        .willThrow(ChannelNotFoundException.withChannelId(channelId));

    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelUpdateRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
  }

  @Test
  @DisplayName("update_private_channel_failed")
  void update_private_channel_failed() throws Exception {
    UUID channelId = UUID.randomUUID();

    ChannelUpdateRequest channelUpdateRequest = new ChannelUpdateRequest(
        "newName",
        "newDescription"
    );

    given(channelService.update(channelId, channelUpdateRequest))
        .willThrow(PrivateChannelUpdateNotAllowedException.withChannelId(channelId));

    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelUpdateRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED"));
  }

  @Test
  @DisplayName("delete_channel_success")
  void delete_channel_success() throws Exception {
    UUID channelId = UUID.randomUUID();

    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

    verify(channelService, times(1)).delete(channelId);
  }

  @Test
  @DisplayName("delete_channel_failed")
  void delete_channel_failed() throws Exception {
    UUID channelId = UUID.randomUUID();

    willThrow(ChannelNotFoundException.withChannelId(channelId))
        .given(channelService)
        .delete(channelId);

    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
  }
}
