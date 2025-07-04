# Logging Support with Log4j 2
The `SailPoint File Upload Utility` now integrates logging using `Apache Log4j 2`, providing a robust and consistent mechanism for capturing and managing application logs. This enhancement enables fine-grained control over log levels, formatting, and output destinations, making it easier for developers and administrators to monitor and troubleshoot the utility in a variety of environments.

By adopting Log4j 2, the utility inherently supports streaming log data to `Security Information and Event Management (SIEM)` solutions such as `Splunk`, `ELK Stack`, or other centralized log aggregation platforms. This is particularly valuable for enterprise deployments, where maintaining visibility, ensuring compliance, and detecting anomalies in real time are critical requirements.

Key benefits of this implementation include:
- **Flexible Configuration:** Customize log levels and appenders (e.g., console, rolling files) via simple configuration changes without modifying the application code.
- **Enterprise Integration:** Seamlessly forward logs to SIEM systems to support centralized monitoring and alerting pipelines.

- **Enhanced Debugging and Auditing:** Provides clear, structured logs to facilitate operational support and forensic analysis.

## Default Configuration
The default configuration for logging is outlined below and logs detailed output to the console and to the file `logs/FileUpload.log`:
```properties
# Root logger option
rootLogger=INFO, STDOUT, LOGFILE

# Define console appender
appender.console=org.apache.log4j.ConsoleAppender
appender.console.name = STDOUT
appender.console.type = Console
appender.console.layout.type = PatternLayout
appender.console.layout.pattern = [%-5level] %msg%n

# Define rolling file appender
appender.file.type = File
appender.file.name = LOGFILE
appender.file.fileName = logs/FileUpload.log
appender.file.layout.type = PatternLayout
appender.file.layout.pattern = [%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n
appender.file.filter.threshold.type = ThresholdFilter
appender.file.filter.threshold.level = info
```

You can override the default configuration by specifying the `-Dlog4j.configurationFile=directory/file.xml` on the command line as outlined below.

## Log4J Configuration Formats
Log4J supports both `properties` format and `xml`.

The above configuration `properties` file can alternatively be provided as an `xml` file as below:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <!-- Console Appender -->
        <Console name="STDOUT" target="SYSTEM_OUT">
            <PatternLayout pattern="[%level] %msg%n"/>
        </Console>

        <!-- Rolling File Appender -->
        <File name="LOGFILE" fileName="logs/FileUpload.log">
            <PatternLayout pattern="[%level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"/>
            <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
        </File>
    </Appenders>

    <Loggers>
        <!-- Root Logger -->
        <Root level="INFO">
            <AppenderRef ref="STDOUT"/>
            <AppenderRef ref="LOGFILE"/>
        </Root>
    </Loggers>
</Configuration>
```

Both the `properties` and the `xml` file achieve the same result which is:
- Output runtime messages to the console.
- Log runtime messages to a file `logs/FileUpload.log` with a little more detail.
- Both output the log level of INFO.

## Custom Logging
As previously mentioned you can provide your own log configuration file that will override the default log configuration.

This can be easily achieved by adding `-Dlog4j.configurationFile=directory/file.xml` to the command line and provinding a valid Log4Js `properties` or `xml` file, for example:
- `java -Dlog4j.configurationFile=directory/file.xml -jar sailpoint-file-upload-utility.jar <commands>`
- `java -Dlog4j.configurationFile=directory/file.properties -jar sailpoint-file-upload-utility.jar <commands>`

# Security Information and Event Management (SIEM)
`Apache Log4j 2` is designed to be highly extensible and integrates smoothly with `SIEM` systems without requiring significant custom development. Out of the box, Log4j 2 provides multiple mechanisms to stream or forward logs to external systems like `Splunk`, `ELK (Elasticsearch, Logstash, Kibana)`, `QRadar`, and other SIEM platforms.

## Basic Integration (Splunk) Example
The following example and instructions facilitate logging from the `SailPoint File Upload Utility` to an instance of `Splunk` by providing a custom `Log4j 2 Configuration File` on the command line.

This example assumes you have an instance of Splunk available that has:
1) A HTTP Event Collector enabled [Splunk HEC Documentation](https://dev.splunk.com/enterprise/docs/devtools/httpeventcollector/).
2) An access token for the HTTP Event Collector.
3) Firewall rules from the File Uploader machine to the Splunk instance.

### Log4j Configuration
The following outlines a configuration file that does the following:
- Prints the log output to the console.
- Appends the log output to the file `logs/FileUpload.log` with more detail.
- Pushes the log output to the `Splunk` server outlined in the appenders section.
- All output the log level of DEBUG for testing purposes.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <!-- Console Appender -->
        <Console name="STDOUT" target="SYSTEM_OUT">
            <PatternLayout pattern="[%level] %msg%n"/>
        </Console>

        <!-- Rolling File Appender -->
        <File name="LOGFILE" fileName="logs/FileUpload.log">
            <PatternLayout pattern="[%level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"/>
            <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
        </File>

        <!-- Splunk HTTP Event Collector -->
        <!-- Update the values below containing 'Splunk:' with the details of your environment -->
        <Http name="SPLUNK" url="https://<<<Splunk:Instance>>>/services/collector/raw">
                <Property name="Authorization" value="Splunk <<<Splunk:Access Token>>>"/>
                <Property name="Content-Type" value="application/json"/>
                <PatternLayout pattern="[%level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n"/>
            <Filters>
                <!-- Filter out the console formatting lines -->
                <RegexFilter regex="------------------------------------------------------------------------------------------------------------" onMatch="DENY" onMismatch="NEUTRAL" />
            </Filters>
        </Http>
    </Appenders>

    <Loggers>
        <!-- Root Logger -->
        <Root level="DEBUG"> <!-- Set this to INFO or WARN for production-->
            <AppenderRef ref="STDOUT"/>
            <AppenderRef ref="LOGFILE"/>

            <!-- Add the SPLUNK logger to the root -->
            <AppenderRef ref="SPLUNK"/>
        </Root>
    </Loggers>
</Configuration>

```

### Command Line Arguments
As previously mentioned you can override the default configuration by specifying the `-Dlog4j.configurationFile=directory/file.xml` on the command line to point to your custom `Splunk` log configuration above:

```bash
java -Dlog4j.configurationFile=/path/to/splunk-logging.xml -jar /path/to/sailpoint-file-upload-utility-4.1.1-all.jar <commands>
```

Example:
```bash
java -Dlog4j.configurationFile=/var/uploader/splunk-logging.xml -jar /var/uploader/sailpoint-file-upload-utility-4.1.1-all.jar --config-file /var/uploader/my-config.json

```

# Further Log Configuration
For more details on configuring logging for your environment using `Log4j`, refer to the [Log4j 2 Documentation](https://logging.apache.org/log4j/2.x/manual/configuration.html) or utilize the examples outlined above.