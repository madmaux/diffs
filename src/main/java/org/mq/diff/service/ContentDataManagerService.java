package org.mq.diff.service;

import javax.transaction.Transactional;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.mq.diff.domain.Base64BinaryData;
import org.mq.diff.domain.Base64BinaryDataId;
import org.mq.diff.domain.Side;
import org.mq.diff.dto.ContentDTO;
import org.mq.diff.dto.DiffEdDTO;
import org.mq.diff.exception.IsNotABase64DataException;
import org.mq.diff.exception.MissingDataParameterException;
import org.mq.diff.repository.Base64BinaryDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * ContentDataManagerService
 *
 * @author MQ
 *
 */
@Service
public class ContentDataManagerService implements DataManager {
  private static final String VALID_BASE64_REGEX = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";

  @Autowired
  private Base64BinaryDataRepository base64BinaryDataRepository;

  @Autowired
  private MapperFacade mapperFacade;

  /**
   *
   * save: this function is in charge of persist the left and right operands to the database
   *
   * @param diffId
   * @param diffEdDTO
   * @param side
   * @return ContentDTO
   */
  @Transactional
  @Override
  public ContentDTO save(String diffId, DiffEdDTO diffEdDTO, Side side) {

    checkIfValidParam(diffEdDTO);

    var base64BinaryData = Base64BinaryData.builder()
        .id(Base64BinaryDataId.builder()
            .diffId(diffId)
            .side(side)
            .build()
        )
        .data(diffEdDTO.getData())
        .build();

    base64BinaryData = base64BinaryDataRepository.save(base64BinaryData);
    return mapperFacade.map(base64BinaryData, ContentDTO.class);
  }

  /**
   *
   * checkIfValidParam: check the request params for validation
   *
   * @param diffEdDTO
   */
  private void checkIfValidParam(DiffEdDTO diffEdDTO) {
    if (diffEdDTO == null || StringUtils.isEmpty(diffEdDTO.getData())) {
      throw new MissingDataParameterException();
    }else if (!diffEdDTO.getData().matches(VALID_BASE64_REGEX)) {
      throw new IsNotABase64DataException();
    }
  }
}
