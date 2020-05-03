package pl.edu.pwr.master.downloader.maven;


import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MavenDependencyDownloader {

    private static final Logger LOGGER = Logger.getLogger(MavenDependencyDownloader.class.getName());
    private static final String POM_NAME = "pom.xml";
    public static final String DEFAULT_OUTPUT_DEPENDENCY_DIR;

    static {
        DEFAULT_OUTPUT_DEPENDENCY_DIR = System.getProperty("user.home") + File.separator + "java-metrics-dependencies";
    }

    private String pomPath;
    private String outputPath;

    public MavenDependencyDownloader(String pomPath, String outputPath) {
        this.pomPath = cleansePomPath(pomPath);
        this.outputPath = outputPath;
    }

    public boolean downloadDependencies() throws IOException, InterruptedException {
        prepareDirectoryTree(this.outputPath);
        clearExistingJars(this.outputPath);

        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info("Preparing pom.xml...");

        PomXmlParser pomXmlParser = new PomXmlParser();
        boolean hasModifiedPom = false;
        try {
            hasModifiedPom = pomXmlParser.parsePom(pomPath + File.separator + POM_NAME, outputPath);
        }
        catch (ParserConfigurationException | SAXException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info("Downloading dependencies to " + outputPath);

        String mvnCommand = "mvn";
        if (System.getProperty("os.name").startsWith("Windows"))
            mvnCommand = "mvn.cmd";
        ProcessBuilder pb = new ProcessBuilder(mvnCommand, "dependency:copy-dependencies", "-DoutputDirectory=" + outputPath);
        pb.directory(new File(pomPath));
        pb.environment().put("MAVEN_OPTS", "-Xms512m -Xmx1024m");
        pb.redirectErrorStream(true);

        Process process = pb.start();
        showProcessLog(process);

        int exitCode = process.waitFor();
        process.destroy();
        if (LOGGER.isLoggable(Level.INFO))
            LOGGER.info(String.format("Download process exited with code %d. You can find the downloaded dependencies in %s.", exitCode, outputPath));

        if (hasModifiedPom) {
            LOGGER.info("Moving pom.xml from backup...");
            pomXmlParser.getPomFromBackup(pomPath + File.separator + POM_NAME);
        }

        return exitCode == 0;
    }

    private String cleansePomPath(String pomPath) {
        if (pomPath.endsWith(POM_NAME)) {
            return pomPath.substring(0, pomPath.length() - POM_NAME.length());
        }
        return pomPath;
    }

    private void clearExistingJars(String path) {
        File d = new File(path);
        if (d.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(d.listFiles((dir, name) -> name.endsWith(".jar")))).forEach(File::delete);
        }
    }

    private void prepareDirectoryTree(String path) {
        File f = new File(path);
        if (!f.exists()) {
            if (f.mkdirs()) {
                if (LOGGER.isLoggable(Level.INFO))
                    LOGGER.info("Directory created: " + path);
            } else {
                if (LOGGER.isLoggable(Level.SEVERE))
                    LOGGER.severe(String.format("Directory %s cannot be created! It might already exist!", path));
            }
        }
    }

    private void showProcessLog(Process process) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.info(line);
        }
    }
}
