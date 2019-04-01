package org.mq.diff.config.logging;

import com.fasterxml.jackson.core.JsonGenerator;

import net.logstash.logback.decorate.JsonGeneratorDecorator;

/**
 *
 * PrettyPrinterDecorator: Decorates log output in a json fashion
 *
 * @autor Logback
 *
 */
public class PrettyPrinterDecorator implements JsonGeneratorDecorator {

  @Override
  public JsonGenerator decorate(JsonGenerator generator) {
    return generator.useDefaultPrettyPrinter();
  }
}
