package pl.edu.pwr;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import pl.edu.pwr.core.Mode;
import pl.edu.pwr.filter.Filter;
import pl.edu.pwr.filter.Reader;
import pl.edu.pwr.flow.DependencyFlow;
import pl.edu.pwr.flow.Flow;
import pl.edu.pwr.flow.PlainFlow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;

@Command(
    name = "JavaMetrics",
    mixinStandardHelpOptions = true,
    version = "javametrics 2.0",
    description = "Extracts Java source code metrics.")
public class App implements Runnable {

  @Option(
      names = {"-i", "--input"},
      description = "input file or directory to parse",
      required = true)
  private File input;

  @Option(
      names = {"-o", "--output"},
      description = "output file (default: input name with '_java-metrics.csv' suffix)")
  private File output;

  @Option(
      names = {"-f", "--filter"},
      description = "file with entities to be included in the output")
  private File filter;

  @Option(
      names = {"-m", "--mode"},
      description = "parsing mode (PLAIN or WITH_DEPENDENCIES)")
  private Mode mode = Mode.PLAIN;

  @Option(
      names = {"-v", "--verbose"},
      description = "enable verbose progress and debug output")
  private boolean verbose = false;

  public static void main(String[] args) {
    new CommandLine(new App()).execute(args);
  }

  @Override
  public void run() {
    // TODO: implement
    if (mode == Mode.WITH_DEPENDENCIES) {
      System.err.println("Currently, WITH_DEPENDENCIES mode is not supported, falling back to PLAIN...");
      mode = Mode.PLAIN;
    }
    startupBanner();
    applyVerbosity();
    Filter f = null;
    if (filter != null) {
      f = Reader.getFilter(Path.of(filter.getAbsolutePath()));
    }

    try {
      Flow flow = setup(f);
      flow.process();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Flow setup(Filter filter) throws IOException {
    return switch (mode) {
      case PLAIN -> new PlainFlow(input.getAbsolutePath(), getOutputName(), filter);
      case WITH_DEPENDENCIES -> new DependencyFlow(input.getAbsolutePath(), getOutputName(), filter);
    };
  }

  private void applyVerbosity() {
    if (verbose) {
      setLogLevel(Level.ALL);
    } else {
      setLogLevel(Level.WARNING);
    }
  }

  private void setLogLevel(Level level) {
    Arrays.stream(LogManager.getLogManager().getLogger("").getHandlers())
        .forEach(h -> h.setLevel(level));
  }

  private String getOutputName() {
    if (output == null) {
      return input.getName() + "_java-metrics.csv";
    }
    return output.getAbsolutePath();
  }

  private void startupBanner() {
    System.out.println(banner());
    System.out.println("-️ input: " + input.getAbsolutePath());
    System.out.println("-️ output: " + getOutputName());
    if (filter != null) {
      System.out.println("-️ filter: " + filter.getAbsolutePath());
    }
    if (mode == Mode.PLAIN) {
      System.out.println("-️ mode: PLAIN");
    } else if (mode == Mode.WITH_DEPENDENCIES) {
      System.out.println("-️ mode: WITH DEPENDENCIES");
    }
    if (verbose) {
      System.out.println("-️ verbose: on");
    } else {
      System.out.println("-️ verbose: off");
    }
  }

  private String banner() {
    return """
            
                   __                  __  ___     __       _         \s
                  / /___ __   ______ _/  |/  /__  / /______(_)_________
             __  / / __ `/ | / / __ `/ /|_/ / _ \\/ __/ ___/ / ___/ ___/
            / /_/ / /_/ /| |/ / /_/ / /  / /  __/ /_/ /  / / /__(__  )\s
            \\____/\\__,_/ |___/\\__,_/_/  /_/\\___/\\__/_/  /_/\\___/____/ \s
                                                                      \s
            
            """;
  }
}
