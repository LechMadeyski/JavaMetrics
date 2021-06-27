package pl.edu.pwr.master;

import com.github.javaparser.ParseException;
import org.apache.commons.cli.*;
import pl.edu.pwr.master.core.MetricsRunner;
import pl.edu.pwr.master.downloader.git.GitDownloader;
import pl.edu.pwr.master.input.CsvReader;
import pl.edu.pwr.master.input.Input;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final String INPUT_FILE_OPTION = "i";
    private static final String CSV_OPTION = "csv";
    private static final String GIT_SOURCES_OUTPUT_DIRECTORY_OPTION = "gitsources";
    private static final String OUTPUT_FILE_OPTION = "o";
    private static final String HELP_OPTION = "h";
    private static final String LOGGING_OPTION = "l";
    private static final String DOWNLOAD_OPTION = "d";
    private static final String DOWNLOAD_ONLY_OPTION = "downloadonly";
    private static final String DEPENDENCY_PARSING_OPTION = "deps";

    public static void main(String[] args) throws IOException, ParseException {
        Options options = prepareOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(HELP_OPTION)) {
                printHelp(options);
                System.exit(1);
            }

            if (!line.hasOption(LOGGING_OPTION)) {
                LogManager.getLogManager().getLogger("").setLevel(Level.WARNING);
            } else {
                LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
            }

            if (line.hasOption(CSV_OPTION) && (line.hasOption(DOWNLOAD_OPTION) || line.hasOption(DOWNLOAD_ONLY_OPTION))) {
                Map<String, Set<String>> repos = CsvReader.getRepositories(line.getOptionValue(CSV_OPTION));
                repos.forEach((repo, v) -> {
                    v.forEach(hash -> {
                        if (LOGGER.isLoggable(Level.INFO))
                            LOGGER.info("Getting repository " + repo + " for hash " + hash);

                        GitDownloader g = null;
                        if (hash != null && !hash.isEmpty()) {
                            g = new GitDownloader(repo, hash);
                        } else {
                            g = new GitDownloader(repo);
                        }
                        if (line.hasOption(GIT_SOURCES_OUTPUT_DIRECTORY_OPTION))
                            g.setOutputDirectory(line.getOptionValue(GIT_SOURCES_OUTPUT_DIRECTORY_OPTION));

                        g.checkout();
                    });
                });

                if (line.hasOption(DOWNLOAD_ONLY_OPTION)) {
                    System.err.println("Download successful!");
                    System.exit(0);
                }
            }

            String inputPath = "";
            if (line.hasOption(INPUT_FILE_OPTION))
                inputPath = line.getOptionValue(INPUT_FILE_OPTION);
            else if (line.hasOption(CSV_OPTION)) {
                if (line.hasOption(GIT_SOURCES_OUTPUT_DIRECTORY_OPTION))
                    inputPath = line.getOptionValue(GIT_SOURCES_OUTPUT_DIRECTORY_OPTION);
                else
                    inputPath = GitDownloader.DEFAULT_OUTPUT_REPOSITORY_DIR;
            }

            boolean parseDependencies = false;

            if (line.hasOption(DEPENDENCY_PARSING_OPTION))
                parseDependencies = true;

            if (line.hasOption(CSV_OPTION)) {
                Input input = CsvReader.getInputToParse(new File(line.getOptionValue(CSV_OPTION)));
                new MetricsRunner(inputPath,
                        line.getOptionValue(OUTPUT_FILE_OPTION, "output.csv"), input, parseDependencies);
            } else {
                new MetricsRunner(inputPath,
                        line.getOptionValue(OUTPUT_FILE_OPTION, "output.csv"), parseDependencies);
            }
        } catch (org.apache.commons.cli.ParseException ex) {
            System.err.println(ex.getMessage());
            printHelp(options);
        } catch (com.github.javaparser.ParseException ex) {
            LOGGER.severe("Parsing a file failed.  Reason: " + ex.getMessage());
        }

        LOGGER.info("Done!");
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("JavaMetrics", options);
    }

    private static Options prepareOptions() {
        Options options = new Options();
        Option inputFileOption = Option.builder(INPUT_FILE_OPTION)
                .argName(INPUT_FILE_OPTION)
                .desc("input directory with source files")
                .required()
                .hasArg()
                .build();

        Option csvOption = Option.builder(CSV_OPTION)
                .argName(CSV_OPTION)
                .desc("csv input file")
                .required()
                .hasArg()
                .build();

        Option gitSourcesOutputDirectoryOption = Option.builder(GIT_SOURCES_OUTPUT_DIRECTORY_OPTION)
                .argName(GIT_SOURCES_OUTPUT_DIRECTORY_OPTION)
                .desc("output directory with sources downloaded based on csv file [-csv flag]")
                .hasArg()
                .build();

        OptionGroup inputOptionGroup = new OptionGroup();
        inputOptionGroup.setRequired(true);
        inputOptionGroup.addOption(inputFileOption);
        inputOptionGroup.addOption(csvOption);

        Option outputFileOption = Option.builder(OUTPUT_FILE_OPTION)
                .argName(OUTPUT_FILE_OPTION)
                .desc("output file")
                .hasArg()
                .build();

        Option helpOption = new Option(HELP_OPTION, "print help message");
        Option loggingOption = new Option(LOGGING_OPTION, "turn on logging");
        Option downloadOption = new Option(DOWNLOAD_OPTION, "turn on sources download before parsing");
        Option downloadOnlyOption = new Option(DOWNLOAD_ONLY_OPTION, "turn on only sources download");

        Option dependencyParsingOption = new Option(DEPENDENCY_PARSING_OPTION, "[EXPERIMENTAL] turn on dependency parsing");

        options.addOptionGroup(inputOptionGroup);
        options.addOption(gitSourcesOutputDirectoryOption);
        options.addOption(outputFileOption);
        options.addOption(helpOption);
        options.addOption(loggingOption);
        options.addOption(downloadOption);
        options.addOption(downloadOnlyOption);
        options.addOption(dependencyParsingOption);
        return options;
    }
}
