package org.mq.diff.config.mapping;

import java.util.List;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.mq.diff.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * MappingConfig: mapper configuration to convert data from DTO to Domain and vice versa
 *
 * @author MQ
 *
 */
@Configuration
public class MappingConfig {

  /**
   *
   * List of mappers components defined in org.mq.diff.mapper
   *
   */
  @Autowired
  private List<Mapper> mappers;

  /**
   *
   * mapperFacade: bean holding all registered mappers defined in mappers list
   *
   * @return MapperFacade
   */
  @Bean
  public MapperFacade mapperFacade() {
    var mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();
    registerMappingDefinitions(mapperFactory);

    return mapperFactory.getMapperFacade();
  }

  /**
   *
   * registerMappingDefinitions: register mapping definitions for each mapper defined in mappers list
   *
   * @param mapperFactory
   */
  private void registerMappingDefinitions(MapperFactory mapperFactory) {
    mappers.forEach(mapper -> mapper.map(mapperFactory).mapNulls(false));
  }
}
