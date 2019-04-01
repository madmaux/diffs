package org.mq.diff.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mq.diff.domain.Side;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiffResultDTO {

  private String diffId;
  private Map<Integer, Integer> diffsLocations;
  private Map<Side, String> data;
}
