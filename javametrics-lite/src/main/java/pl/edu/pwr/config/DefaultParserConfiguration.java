package pl.edu.pwr.config;

import com.github.javaparser.ParserConfiguration;

public class DefaultParserConfiguration {

  private DefaultParserConfiguration() {}

  public static ParserConfiguration getInstance() {
    return new ParserConfiguration().setAttributeComments(true);
  }
}
