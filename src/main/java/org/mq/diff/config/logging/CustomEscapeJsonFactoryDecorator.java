package org.mq.diff.config.logging;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import net.logstash.logback.decorate.JsonFactoryDecorator;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

/**
 *
 * CustomEscapeJsonFactoryDecoder: Decorates log output in a json fashion
 *
 * @autor Logback
 *
 */
public class CustomEscapeJsonFactoryDecorator implements JsonFactoryDecorator {

  private static class CustomCharacterEscapes extends CharacterEscapes {

    private static final long serialVersionUID = 1L;
    private static final SerializedString ESCAPE_LF = new SerializedString(System.lineSeparator());
    private static final SerializedString ESCAPE_TAB = new SerializedString("\u0009");
    private static final SerializedString EMPTY = new SerializedString(StringUtils.EMPTY);
    private static final SerializedString ESCAPE = new SerializedString("\\");

    private final int[] escapeCodesForAscii;

    private CustomCharacterEscapes() {
      escapeCodesForAscii = standardAsciiEscapesForJSON();
      escapeCodesForAscii[(int) '\n'] = ESCAPE_CUSTOM;
      escapeCodesForAscii[(int) '\r'] = ESCAPE_CUSTOM;
      escapeCodesForAscii[(int) '\t'] = ESCAPE_CUSTOM;
      escapeCodesForAscii[(int) '\\'] = ESCAPE_CUSTOM;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
      switch (ch) {
        case '\n':
          return ESCAPE_LF;
        case '\r':
          return EMPTY;
        case '\t':
          return ESCAPE_TAB;
        case '\\':
          return ESCAPE;
        default:
          return new SerializedString(new String(new char[]{(char) ch}));
      }
    }

    @Override
    public int[] getEscapeCodesForAscii() {
      return escapeCodesForAscii;
    }
  }

  @Override
  public MappingJsonFactory decorate(MappingJsonFactory factory) {
    factory.setCharacterEscapes(new CustomCharacterEscapes());
    return factory;
  }
}
