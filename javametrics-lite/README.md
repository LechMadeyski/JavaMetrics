# JavaMetrics

![test workflow](https://github.com/arekziobrowski/JavaMetrics/actions/workflows/test.yml/badge.svg)

Java source code metric derivation tool.

## Using the tool
To use the JavaMetrics, build it with Maven:
```shell
mvn package
```
For help, run the following command:
```shell
> java -jar java-metrics-1.0-SNAPSHOT-jar-with-dependencies.jar --help
Usage: JavaMetrics [-hvV] [-f=<filter>] -i=<input> [-m=<mode>] [-o=<output>]
Extracts Java source code metrics.
  -f, --filter=<filter>   file with entities to be included in the output
  -h, --help              Show this help message and exit.
  -i, --input=<input>     input file or directory to parse
  -m, --mode=<mode>       parsing mode (PLAIN or WITH_DEPENDENCIES)
  -o, --output=<output>   output file (default: input name with '_java-metrics.
                            csv' suffix)
  -v, --verbose           enable verbose progress and debug output
  -V, --version           Print version information and exit.
```
