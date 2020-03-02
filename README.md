# JavaMetrics
JavaMetrics tool calculates a set of software metrics that may be useful to measure software quality and/or to use as predictors in software defect or code smell prediction models.

### Usage
Before using JavaMetrics make sure you have the latest jar file available. If you do not have one, you can build it yourself with Maven.

#### Building the JavaMetrics
In order to build the tool, check-out this repository.

1. Go to the root directory of this checked-out repository (the one with `pom.xml` file in it),
2. Invoke `mvn clean package` in your command line,
3. The resulting jar file named `java-metrics-1.0-SNAPSHOT-jar-with-dependencies.jar` should be in the `target` directory of the root directory.

#### Running the JavaMetrics
You can run the JavaMetrics with your ordinary Java Runtime Environment. In order to do so invoke the following command in your command line:
```
java -jar java-metrics-1.0-SNAPSHOT-jar-with-dependencies.jar [flags and arguments]
```

There are several flags that let users tweak the behaviour of JavaMetrics to their needs. All flags are available by invoking:
```
java -jar java-metrics-1.0-SNAPSHOT-jar-with-dependencies.jar -h
```

#### JavaMetrics flags
Here is an extended summary of flags available in JavaMetrics tool.
* `-h`  
    Prints the help for the tool. It contains the short summary of available flags.
    * * *
* `-o [output file]`  
    Name and location of the output file with metrics.
    * * *
* `-i [input directory]`  
    Input directory with source files. It can be the directory with the project or a directory containing multiple projects. **Be careful! Only one of the [-i | -csv] flags can be used for providing the input!**
      
    Example 1 (single project):
    ```
    java -jar target/java-metrics-1.0-SNAPSHOT-jar-with-dependencies.jar -i /home/arek/java-metrics-source-repos/apache/syncope -o /home/arek/output.csv
    ```
      
    Example 2 (multiple projects):
    ```
    arek@arek-lenovo:~/java-metrics-source-repos$ ls apache
        syncope
        tez
        tika
    ```
    ```
    java -jar target/java-metrics-1.0-SNAPSHOT-jar-with-dependencies.jar -i /home/arek/java-metrics-source-repos/apache -o /home/arek/output.csv
    ```
    * * *
* `-csv [csv name]`  
    Flag for providing the input csv (comma separated value) to the tool. The CSV format is available [here](https://github.com/LechMadeyski/MSc19Ziobrowski/blob/master/examples/calculate_metrics.csv). You may need to use our conforming tool in order to conform your input CSV values in the rows to the format. Default location of repositories on your disk that will are specified in the CSV is `$HOME/java-metrics-source-repos`.  
    
    The repositories should be present there before running the tool or you can download the repositories via git by specifying `-d` flag. One can provide own directory with source codes that are specified in the CSV file by using `-gitsources` flag.  

    Example 1 (conforming the CSV with jar from csv-conform):
    ```
    java -jar csv-conform-1.0-SNAPSHOT-jar-with-dependencies.jar /home/arek/input_csv.csv /home/arek/output_csv.csv
    ```  
    **Be careful!** Specifying only one argument to the `csv-conform` will overwrite your input csv to its conformed version.
      
    Example 2 (parse repositories with input csv):
    ```
    java -jar target/java-metrics-1.0-SNAPSHOT-jar-with-dependencies.jar -csv /home/arek/csv_to_parse.csv -o /home/arek/output.csv
    ```
    * * *
* `-d`  
    Turn on sources download. It has to be used with `-csv` flag. The default directory where the sources will be downloaded is `$HOME/java-metrics-source-repos`. You specify your own target directory by using `-gitsources` flag.  

    Example:
    ```
    java -jar target/java-metrics-1.0-SNAPSHOT-jar-with-dependencies.jar -csv /home/arek/example_input.csv -d -o /home/arek/output.csv
    ```
    **Be careful!** If the target directory where the sources will be downloaded already contains a directory with the project, the tool won't download it.
* `-gitsources [output directory of downloaded sources]`  
    Output directory with sources downloaded based on the `-csv` flag. The default location where the sources will be downloaded is `$HOME/java-metrics-source-repos`.  
    
    Example:
    ```
    java -jar target/java-metrics-1.0-SNAPSHOT-jar-with-dependencies.jar -csv /home/arek/example_input.csv -d -gitsources /home/arek/input_sources_directory -o /home/arek/output.csv
    ```
* `-l`  
    Turn on logging. Logs from the JavaMetrics will be available on the standard output.

