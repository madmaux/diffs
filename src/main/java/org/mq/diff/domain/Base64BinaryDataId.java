package org.mq.diff.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Embeddable
public class Base64BinaryDataId implements Serializable {

  @Column(name = "diffId", length = 50)
  @NotNull
  private String diffId;

  @Column(name = "side")
  @Enumerated(EnumType.STRING)
  @NotNull
  private Side side;
}
