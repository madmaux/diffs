package org.mq.diff.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

public interface Mapper<T, R> {

  ClassMapBuilder<T, R> map(MapperFactory mapper);
}
