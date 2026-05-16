package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "binary_contents")
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "size", nullable = false)
  private Long size;

  @Column(name = "content_type", nullable = false)
  private String contentType;

  public BinaryContent(String fileName, long size, String contentType) {
    if (fileName == null || fileName.isBlank()) {
      throw new IllegalArgumentException("fileName is blank.");
    }

    if (size < 0) {
      throw new IllegalArgumentException("size is negative.");
    }

    if (contentType == null || contentType.isBlank()) {
      throw new IllegalArgumentException("contentType is blank.");
    }

    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}
