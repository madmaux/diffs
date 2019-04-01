package org.mq.diff.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "Base64BinaryData")
public class Base64BinaryData {
  @EmbeddedId
  private Base64BinaryDataId id;

  @Column( name = "data" )
  @Lob
  @NotNull
  private String data;
}
