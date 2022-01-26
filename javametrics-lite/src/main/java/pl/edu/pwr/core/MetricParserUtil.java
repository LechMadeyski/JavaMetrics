package pl.edu.pwr.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import pl.edu.pwr.core.exception.JavaParserNotConfiguredException;

public class MetricParserUtil {

  private static volatile JavaParser javaParser;
  private static String path;

  private MetricParserUtil() {}

  public static void setup(ParserConfiguration configuration, String projectPath) {
    if (javaParser == null) {
      synchronized (MetricParserUtil.class) {
        if (javaParser == null) {
          javaParser = new JavaParser(configuration);
          path = projectPath;
        }
      }
    }
  }

  public static JavaParser getJavaParserInstance() throws JavaParserNotConfiguredException {
    if (javaParser == null) {
      synchronized (MetricParserUtil.class) {
        if (javaParser == null) {
          throw new JavaParserNotConfiguredException("Static Javaparser has not been configured.");
        }
      }
    }
    return javaParser;
  }

  public static String getProjectPath() {
    return path;
  }
}
