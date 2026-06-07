package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "channels")
@Getter
@Setter
@ToString(callSuper = true, exclude = "readStatuses")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private ChannelType type;

  @Default
  @OneToMany(mappedBy = "channel")
  private List<ReadStatus> readStatuses = new ArrayList<>();

  public Channel(String name, String description, ChannelType type) {
    if (type == null) {
      throw new IllegalArgumentException("type is null.");
    }

    if (type == ChannelType.PUBLIC) {
      if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("name is blank.");
      }
      if (description == null) {
        throw new IllegalArgumentException("description is null.");
      }
    }

    this.type = type;
    this.name = name;
    this.description = description;
  }
}
