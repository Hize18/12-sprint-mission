package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  private Channel createChannel(String name) {
    return Channel.builder()
        .name(name)
        .description(name + "_description")
        .type(ChannelType.PUBLIC)
        .build();
  }

  private User createUser(String username) {
    return User.builder()
        .username(username)
        .email(username + "@test.com")
        .password("password")
        .build();
  }

  private Message createMessage(Channel channel, User author, String content) {
    return Message.builder()
        .channel(channel)
        .author(author)
        .content(content)
        .attachments(List.of())
        .build();
  }

  @Test
  @DisplayName("findDetailById_success")
  void findDetailById_success() {
    User author = testEntityManager.persistAndFlush(createUser("author"));
    Channel channel = testEntityManager.persistAndFlush(createChannel("channel"));

    Message createdMessage = messageRepository.save(createMessage(channel, author, "test_content"));
    testEntityManager.flush();
    testEntityManager.clear();

    Optional<Message> message = messageRepository.findDetailById(createdMessage.getId());

    assertThat(message).isPresent();
    Message foundMessage = message.get();
    assertThat(foundMessage.getContent()).isEqualTo("test_content");
    assertThat(foundMessage.getAuthor().getId()).isEqualTo(author.getId());
    assertThat(foundMessage.getChannel().getId()).isEqualTo(channel.getId());
  }

  @Test
  @DisplayName("findDetailById_failed")
  void findDetailById_failed() {
    Optional<Message> message = messageRepository.findDetailById(UUID.randomUUID());

    assertThat(message).isEmpty();
  }

  @Test
  @DisplayName("findLastMessageAtByChannelIds")
  void findLastMessageAtByChannelIds_success() {
    User author = testEntityManager.persistAndFlush(createUser("author"));
    Channel channel = testEntityManager.persistAndFlush(createChannel("channel"));

    Message createdMessage = messageRepository.save(createMessage(channel, author, "test_content"));
    testEntityManager.flush();
    testEntityManager.clear();

    Map<UUID, Instant> lastMessageMap = new HashMap<>();
    messageRepository.findLastMessageAtByChannelIds(List.of(channel.getId()))
        .forEach(obj -> lastMessageMap.put((UUID) obj[0], (Instant) obj[1]));

    assertThat(lastMessageMap.size()).isEqualTo(1);
    assertThat(lastMessageMap.containsKey(channel.getId()));
  }

  @Test
  @DisplayName("findLastMessageAtByChannelIds_failed")
  void findLastMessageAtByChannelIds_failed() {
    Map<UUID, Instant> lastMessageMap = new HashMap<>();
    messageRepository.findLastMessageAtByChannelIds(List.of())
        .forEach(obj -> lastMessageMap.put((UUID) obj[0], (Instant) obj[1]));

    assertThat(lastMessageMap.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("findAllDetailByIdIn_success")
  void findAllDetailByIdIn_success() {
    User author = testEntityManager.persistAndFlush(createUser("author"));
    Channel channel = testEntityManager.persistAndFlush(createChannel("channel"));

    Message createdMessage1 = messageRepository.save(
        createMessage(channel, author, "test_content1"));
    Message createdMessage2 = messageRepository.save(
        createMessage(channel, author, "test_content2"));
    testEntityManager.flush();
    testEntityManager.clear();

    List<Message> messages = messageRepository
        .findAllDetailByIdIn(List.of(createdMessage1.getId(), createdMessage2.getId()));

    assertThat(messages.size()).isEqualTo(2);
    assertThat(messages.contains(createdMessage1));
    assertThat(messages.contains(createdMessage2));
  }

  @Test
  @DisplayName("findAllDetailByIdIn_failed")
  void findAllDetailByIdIn_failed() {
    List<Message> messages = messageRepository.findAllDetailByIdIn(List.of());

    assertThat(messages.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("findIdsByChannelId_success")
  void findIdsByChannelId_success() {
    User author = testEntityManager.persistAndFlush(createUser("author"));
    Channel channel = testEntityManager.persistAndFlush(createChannel("channel"));

    Message createdMessage1 = messageRepository.save(
        createMessage(channel, author, "test_content1"));
    Message createdMessage2 = messageRepository.save(
        createMessage(channel, author, "test_content2"));
    testEntityManager.flush();
    testEntityManager.clear();

    Slice<UUID> messageIds = messageRepository.findIdsByChannelId(
        channel.getId(),
        PageRequest.of(0, 10)
    );

    assertThat(messageIds.getContent().size()).isEqualTo(2);
    assertThat(messageIds.getContent().contains(createdMessage1.getId())).isTrue();
    assertThat(messageIds.getContent().contains(createdMessage2.getId())).isTrue();
  }

  @Test
  @DisplayName("findIdsByChannelId_failed")
  void findIdsByChannelId_failed() {
    Slice<UUID> messageIds = messageRepository.findIdsByChannelId(
        UUID.randomUUID(),
        PageRequest.of(0, 10)
    );

    assertThat(messageIds.getContent().size()).isEqualTo(0);
  }

  @Test
  @DisplayName("findIdsByChannelIdAndCreatedAtLessThan_success")
  void findIdsByChannelIdAndCreatedAtLessThan_success() {
    User author = testEntityManager.persistAndFlush(createUser("author"));
    Channel channel = testEntityManager.persistAndFlush(createChannel("channel"));

    Message createdMessage1 = messageRepository.save(
        createMessage(channel, author, "test_content1"));
    Message createdMessage2 = messageRepository.save(
        createMessage(channel, author, "test_content2"));
    testEntityManager.flush();
    testEntityManager.clear();

    Slice<UUID> messageIds = messageRepository.findIdsByChannelIdAndCreatedAtLessThan(
        channel.getId(),
        Instant.now().plusSeconds(1),
        PageRequest.of(0, 10)
    );

    assertThat(messageIds.getContent().size()).isEqualTo(2);
    assertThat(messageIds.getContent().contains(createdMessage1.getId())).isTrue();
    assertThat(messageIds.getContent().contains(createdMessage2.getId())).isTrue();
  }

  @Test
  @DisplayName("findIdsByChannelIdAndCreatedAtLessThan_failed")
  void findIdsByChannelIdAndCreatedAtLessThan_failed() {
    Slice<UUID> messageIds = messageRepository.findIdsByChannelIdAndCreatedAtLessThan(
        UUID.randomUUID(),
        Instant.now().plusSeconds(1),
        PageRequest.of(0, 10)
    );

    assertThat(messageIds.getContent().size()).isEqualTo(0);
  }
}
