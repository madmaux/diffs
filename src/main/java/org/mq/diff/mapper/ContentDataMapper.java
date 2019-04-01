package org.mq.diff.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.mq.diff.domain.Base64BinaryData;
import org.mq.diff.dto.ContentDTO;
import org.springframework.stereotype.Component;

/**
 *
 * ContentDataMapper: maps data from ContentDTO to Base64BinaryData and vice versa
 *
 * @author MQ
 *
 */
@Component
public class ContentDataMapper implements Mapper<ContentDTO, Base64BinaryData> {

  /**
   *
   * map: return a mapper with the transformation config for ContentDTO to Base64BinaryData
   *
   * @param mapper
   * @return ClassMapBuilder of type ContentDTO and Bas64BinaryData
   */
  @Override
  public ClassMapBuilder<ContentDTO, Base64BinaryData> map(MapperFactory mapper) {
    return mapper.classMap(ContentDTO.class, Base64BinaryData.class)
        .field("diffId", "id.diffId")
        .field("side", "id.side")
        .field("data", "data");
  }
}
