package org.mq.diff.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.mq.diff.domain.Side;
import org.mq.diff.dto.DiffResultDTO;
import org.mq.diff.exception.MissingDiffIdParameterException;
import org.mq.diff.exception.NonDiffIdFoundException;
import org.mq.diff.exception.NotEnoughDataToCompareException;
import org.mq.diff.repository.Base64BinaryDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * DiffDataComparatorService:
 *
 * @author MQ
 *
 */
@Service
public class DiffDataComparatorService implements DataComparator {

  @Autowired
  private Base64BinaryDataRepository base64BinaryDataRepository;

  /**
   *
   * compare: this method with grab both operands left and right from the database and will compared to extract the differences
   *   if the base64 binaries are equals or has different length will always return the right operand
   *   if the two base64 binaries has the same length it will compare it and grab the differences by placing the offset
   *   and length in a map
   *
   * @param diffId
   * @return DiffResultDTO
   */
  @Override
  public DiffResultDTO compare(String diffId) {

    final var diffs = base64BinaryDataRepository.findAllByIdDiffId(diffId);
    checkIfValidParam(diffId, diffs);

    final var leftData = diffs.stream().filter(diff -> Side.LEFT.equals(diff.getId().getSide()))
        .map(diff -> diff.getData()).findFirst().orElse("");

    final var rightData = diffs.stream().filter(diff -> Side.RIGHT.equals(diff.getId().getSide()))
        .map(diff -> diff.getData()).findFirst().orElse("");

    if (leftData.equals(rightData) || leftData.length() != rightData.length()) {
      return DiffResultDTO.builder()
          .diffId(diffId)
          .data(Map.of(Side.RIGHT, rightData))
          .build();
    }

    var diffResultDTO = DiffResultDTO.builder()
        .diffId(diffId)
        .diffsLocations(new HashMap<>())
        .data(Map.of(Side.RIGHT, rightData, Side.LEFT, leftData))
        .build();

    var currOffset = -1;

    for (int index = 0; index < leftData.length(); index++) {
      var charsNotEquals = leftData.charAt(index) != rightData.charAt(index);

      if (charsNotEquals && currOffset < 0) {
        currOffset = index;
        diffResultDTO.getDiffsLocations().put(index, 1);
      } else if (charsNotEquals && currOffset >= 0) {
        var length = diffResultDTO.getDiffsLocations().get(currOffset) + 1;
        diffResultDTO.getDiffsLocations().put(currOffset, length++);
      } else {
        currOffset = -1;
      }
    }
    return diffResultDTO;
  }

  /**
   *
   * checkIfValidParam: check the request params for validation
   *
   * @param id
   * @param diffs
   */
  private void checkIfValidParam(String id, List diffs) {
    if (StringUtils.isEmpty(id)) {
      throw new MissingDiffIdParameterException();
    } else if (diffs.isEmpty()) {
      throw new NonDiffIdFoundException();
    } else if (diffs.size() != 2) {
      throw new NotEnoughDataToCompareException();
    }
  }
}
