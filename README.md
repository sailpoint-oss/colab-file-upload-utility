[![Discourse Topics][discourse-shield]][discourse-url]
[![Issues][issues-shield]][issues-url]
[![Latest Releases][release-shield]][release-url]
[![Contributor Shield][contributor-shield]][contributors-url]

[discourse-shield]:https://img.shields.io/discourse/topics?label=Discuss%20This%20Tool&server=https%3A%2F%2Fdeveloper.sailpoint.com%2Fdiscuss
[discourse-url]:https://developer.sailpoint.com/discuss/t/file-upload-utility/18181
[issues-shield]:https://img.shields.io/github/issues/sailpoint-oss/colab-file-upload-utility?label=Issues
[issues-url]:https://github.com/sailpoint-oss/colab-file-upload-utility/issues
[release-shield]: https://img.shields.io/github/v/release/sailpoint-oss/colab-file-upload-utility?label=Current%20Release
[release-url]:https://github.com/sailpoint-oss/colab-file-upload-utility/releases
[contributor-shield]:https://img.shields.io/github/contributors/sailpoint-oss/colab-file-upload-utility?label=Contributors
[contributors-url]:https://github.com/sailpoint-oss/colab-file-upload-utility/graphs/contributors

# SailPoint File Upload Utility
[Explore the docs »](https://developer.sailpoint.com/discuss/t/file-upload-utility/18181)

[New to the CoLab? Click here »](https://developer.sailpoint.com/discuss/t/about-the-sailpoint-developer-community-colab/11230)

# Overview
The SailPoint File Upload Utility provides a way for SailPoint customers to upload files to Identity Security Cloud automatically, for automatic account and entitlement aggregations. This is an executable Java-based JAR which can be executed with a set of options, or scheduled via CLI utilities like `cron`. This utility uploads files the same way as if you were to upload the files via the admin user interface, by calling the supported SailPoint REST APIs.

# Getting Started

Using File Upload Utility is pretty straight-forward, and follows the pattern of normal Java JAR execution:
```
java -jar sailpoint-file-upload-utility.jar <commands>
```

## Options 

We cover using specific use cases further below, but here are all available options to pass to the File Upload Utility.

| Option                               | Required | Example Usage                                  | Description                                                                                                                                                                                           |
|--------------------------------------|----------|------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `-u <arg>`, `--url <arg>`            | Required | `--url https://example.api.identitynow.com`    | SailPoint API Gateway (e.g. https://tenant.api.identitynow.com)                                                                                                                                       |
| `-i <arg>`, `--clientId <arg>`       | Required | `--clientId d0b...574`                         | SailPoint Client ID (PAT). If value of `env` is provided, then the value for environment variable `SAIL_CLIENT_ID` will be used.                                                                      |
| `-s <arg>`, `--clientSecret <arg>`   | Required | `--clientSecret a34...1df`                     | SailPoint Client Secret (PAT).  If a value is not supplied, will be prompted interactively.  If value of `env` is provided, then the value for environment variable `SAIL_CLIENT_SECRET` will be used. |
| `-f <arg>`, `--file <arg>`           | Required | `--file /Users/neil.mcglennon/test/resources/` | File or directories for bulk aggregation. This can be specified multiple times.                                                                                                                       |
| `-d`, `--disableOptimization`        | Optional | `--disableOptimization`                        | Disable Optimization on Account Aggregation                                                                                                                                                           |
| `-o <arg>`, `--objectType <arg>`     | Optional | `--objectType group`                           | File Type; Account or Entitlement Schema. Default: Account                                                                                                                                            |
| `-R`, `--recursive`                  | Optional | `--recursive`                                  | Recursively search directories                                                                                                                                                                        |
| `-S`, `--simulate`                   | Optional | `--simulate`                                   | Simulation Mode.  Scans for files but does not aggregate.                                                                                                                                             |
| `-t <arg>`, `--timeout <arg>`        | Optional | `--timeout 100000`                             | Timeout (in milliseconds). Default: 10000 (10s)                                                                                                                                                       |
| `-x <arg>`, `--extension <arg>`      | Optional | `--extension csv`                              | File extensions to search (for directories only).  Default: csv                                                                                                                                       |
| `-v`, `--verbose`                    | Optional | `--verbose`                                    | Verbose logging. Default: false                                                                                                                                                                       |
| `-H <arg>`, `--proxyHost <arg>`      | Optional | `--proxyHost proxy.host.com`                   | Proxy host name or IP. Use `--proxyHost` and `--proxyPort` together.                                                                                                                                  |
| `-P <arg>`, `--proxyPort <arg>`      | Optional | `--proxyPort 443`                              | Proxy port. Use `--proxyHost` and `--proxyPort` together.                                                                                                                                             |
| `-U <arg>`, `--proxyUser <arg>`      | Optional | `--proxyUser foo`                              | Proxy user for authenticated proxies. Use `--proxyUser` and `--proxyPassword` together.                                                                                                               |
| `-W <arg>`, `--proxyPassword <arg>`  | Optional | `--proxyPassword bar`                          | Proxy password for authenticated proxies. Use `--proxyUser` and `--proxyPassword` together. If a value is not supplied, will be prompted interactively.                                                                                                          |
| `-V`, `--version`                    | Optional | `--version`                                    | Displays the current version.                                                                                                                                                                         |
| `-h`, `--help`                       | Optional | `--help`                                       | Displays help.                                                                                                                                                                                        |

## Requirements

- **Java** -  is a Java based application and requires Java Development Kit (JDK) 11 or higher to run.  We build and test against OpenJDK 11 as well as OpenJDK 17.
- **Network** - This also requires external, outbound access over HTTPS (443) via REST API calls to the SailPoint Cloud as indicated by the URL you configure the File Upload Utility.

## Help and Usage

In order to see help and usage of the File Upload Utility, supply the `--help` or `-h` options.  The output of this looks like this:

```shell
$ java -jar sailpoint-file-upload-utility.jar --help
Usage:

Perform bulk file aggregations to Identity Security Cloud.

java -jar sailpoint-file-upload-utility.jar [-dhRSvV] [-s[=<clientSecret>]] [-W
[=<proxyPassword>]] [-H=<proxyHost>] -i=<clientId> [-o=<objectType>]
[-P=<proxyPort>] [-t=<timeout>] -u=<url> [-U=<proxyUser>] -f=<files>
[-f=<files>]... [-x=<fileExtensions>]...

Description:

Scans specified files and directories for files in bulk, to send to Identity
Security Cloud for account or entitlement aggregation.  For more details see:
https://developer.sailpoint.com/discuss/t/file-upload-utility/18181

Options:
  -u, --url=<url>           SailPoint API Gateway (e.g. https://tenant.api.
                              identitynow.com)
  -i, --clientId=<clientId> SailPoint Client ID (PAT)
  -s, --clientSecret[=<clientSecret>]
                            SailPoint Client Secret (PAT)
  -f, --file=<files>        File or directories for bulk aggregation.
  -d, --disableOptimization Disable Optimization on Account Aggregation
  -o, --objectType=<objectType>
                            File Type; Account or Entitlement Schema. Default:
                              Account
  -R, --recursive           Recursively search directories
  -S, --simulate            Simulation Mode.  Scans for files but does not
                              aggregate.
  -t, --timeout=<timeout>   Timeout (in milliseconds). Default: 10000 (10s)
  -x, --extension=<fileExtensions>
                            File extensions to search (for directories only).
                              Default: csv
  -v, --verbose             Verbose logging. Default: false
  -H, --proxyHost=<proxyHost>
                            Proxy Host
  -P, --proxyPort=<proxyPort>
                            Proxy Post
  -U, --proxyUser=<proxyUser>
                            Proxy User; Used for authenticated proxies
  -W, --proxyPassword[=<proxyPassword>]
                            Proxy Password; Used for authenticated proxies
  -V, --version             Displays the current version.
  -h, --help                Display help.
```
This command is useful for quick reference in places where documentation is not readily available.

## Version

To see the current version of File Upload Utility, supply the `--version` or `-V` option.  Output should look like this:
```
$ java -jar sailpoint-file-upload-utility.jar --version
SailPoint File Upload Utility 4.1.1
Build: 2024-09-10 16:15 CST
Documentation: https://developer.sailpoint.com/discuss/t/file-upload-utility/18181
JVM: 17.0.10 (Amazon.com Inc. OpenJDK 64-Bit Server VM 17.0.10+7-LTS)
OS: Mac OS X 14.6.1 aarch64
```
This command is useful for quick reference which version of the File Upload Utility you are running, as well as which version of Java is being run, often for troubleshooting processes.

## Aggregating Account Files

To aggregate account files, you'll want to specify the following options:

```shell
$ java -jar sailpoint-file-upload-utility.jar --url https://example.api.identitynow.com --clientId d0b28ce1b2694b64949dd546de1ff574 --clientSecret a34...1df --file /Users/neil.mcglennon/test/resources/ -R
------------------------------------------------------------------------------------------------------------
 SailPoint File Upload Utility
------------------------------------------------------------------------------------------------------------
 Version:            4.1.1                          
 Date:               2024-09-10 16:15 CST           
 Docs:               https://developer.sailpoint.com/discuss/t/file-upload-utility/18181 
------------------------------------------------------------------------------------------------------------
 URL:                https://example.api.identitynow.com 
 Client ID:          d0b28ce1b2694b64949dd546dd1ff574 
 Files:              /Users/neil.mcglennon/test/resources 
 ObjectType:         account                        
 Optimization:       true                           
 Recursive:          true                           
 Extensions:         csv                            
 Simulation:         false                          
 Verbose:            false                          
 Timeout:            10000                          
------------------------------------------------------------------------------------------------------------
Checking credentials...
Analyzing directory: /Users/neil.mcglennon/test/resources
Analyzing account file: 184744-AuthEmployees.csv
	File [184744-AuthEmployees.csv]: Aggregated successfully.
Analyzing account file: 2c918087701c40cf01701dfdf2c61e2a-AuthEmployees.csv
	File [2c918087701c40cf01701dfdf2c61e2a-AuthEmployees.csv]: Aggregated successfully.
Analyzing account file: 184744 - AuthEmployees.csv
	File [184744 - AuthEmployees.csv]: Aggregated successfully.
Analyzing account file: 81260-entitlement.csv
	File [81260-entitlement.csv]: Unable to resolve old source ID reference [81260] to new source ID reference. This file will be skipped.
	File [81260-entitlement.csv]: Does not contain a valid source ID. Skipping...
Analyzing account file: 82343-entitlements.csv
	File [82343-entitlements.csv]: Unable to resolve old source ID reference [82343] to new source ID reference. This file will be skipped.
	File [82343-entitlements.csv]: Does not contain a valid source ID. Skipping...
Analyzing account file: Don't Read.csv
	File [Don't Read.csv]: Does not contain a valid source ID. Skipping...
Analyzing account file: f1f3b747be924745afbc0c8a53f71baf-file-test-account-feed.csv
	File [f1f3b747be924745afbc0c8a53f71baf-file-test-account-feed.csv]: Aggregated successfully.
Analyzing account file: 82343-acounts.csv
	File [82343-acounts.csv]: Unable to resolve old source ID reference [82343] to new source ID reference. This file will be skipped.
	File [82343-acounts.csv]: Does not contain a valid source ID. Skipping...
Analyzing account file: 82343-roles.csv
	File [82343-roles.csv]: Unable to resolve old source ID reference [82343] to new source ID reference. This file will be skipped.
	File [82343-roles.csv]: Does not contain a valid source ID. Skipping...
Analyzing account file: f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv
	File [f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv]: Error: HTTP Code[400] Message[] Body[{"detailCode":"400.1.3 Illegal value","trackingId":"edf48ac42d4a426384dcf0d0196cbfeb","messages":[{"locale":"en-US","localeOrigin":"DEFAULT","text":"Column: \"[displayName, created, description, modified, entitlements, permissions]\" is unknown and/or column: \"[givenName, familyName, e-mail, location, manager]\" is missing"},{"locale":"und","localeOrigin":"REQUEST","text":"Column: \"[displayName, created, description, modified, entitlements, permissions]\" is unknown and/or column: \"[givenName, familyName, e-mail, location, manager]\" is missing"}],"causes":[]}]
Analyzing account file: 81260-entitlement.csv
	File [81260-entitlement.csv]: Unable to resolve old source ID reference [81260] to new source ID reference. This file will be skipped.
	File [81260-entitlement.csv]: Does not contain a valid source ID. Skipping...
Analyzing account file:  184745 - AuthContractors.csv
	File [ 184745 - AuthContractors.csv]: Unable to resolve old source ID reference [184745] to new source ID reference. This file will be skipped.
	File [ 184745 - AuthContractors.csv]: Does not contain a valid source ID. Skipping...
Complete.
------------------------------------------------------------------------------------------------------------
 Elapsed time:       7 seconds                      
 Files processed:    12                             
------------------------------------------------------------------------------------------------------------
 Success:            4                              
------------------------------------------------------------------------------------------------------------
 Error:              1                              
------------------------------------------------------------------------------------------------------------
 Skipped:            7                              
------------------------------------------------------------------------------------------------------------
```


## Aggregating Entitlement Files

To aggregate entitlement files, you'll want to specify the following options:

Execution of this might look like the following:

```shell
$ java -jar sailpoint-file-upload-utility.jar --url https://example.api.identitynow.com --clientId d0b28ce1b2694b64949dd546de1ff574 --clientSecret a34...1df --file /Users/neil.mcglennon/test/resources/entitlements/f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv --objectType group
------------------------------------------------------------------------------------------------------------
 SailPoint File Upload Utility
------------------------------------------------------------------------------------------------------------
 Version:            4.1.1                          
 Date:               2024-09-10 16:15 CST           
 Docs:               https://developer.sailpoint.com/discuss/t/file-upload-utility/18181 
------------------------------------------------------------------------------------------------------------
 URL:                https://example.api.identitynow.com 
 Client ID:          d0b28ce1b2694b64949dd546dd1ff574 
 Files:              /Users/neil.mcglennon/test/resources/entitlements/f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv 
 ObjectType:         group                          
 Recursive:          true                           
 Extensions:         csv                            
 Simulation:         false                          
 Verbose:            true                           
 Timeout:            10000                          
------------------------------------------------------------------------------------------------------------
Checking credentials...
Analyzing group file: /Users/neil.mcglennon/test/resources/entitlements/f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv
Analyzing group file: f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv
	File [f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv]: detected with source ID reference [f1f3b747be924745afbc0c8a53f71baf].
	File [f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv]: Submitting Entitlement Aggregation: Source ID[f1f3b747be924745afbc0c8a53f71baf], Object Type[group]
	File [f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv]: Aggregation response: {"type":"QUARTZ","id":"8f7ec314b9dc47abbbea347398826e07","name":"Cloud Group Aggregation","description":"Aggregates from the specified application.","parentName":null,"launcher":"slpt.services","created":"2024-09-11T02:11:51.912+00:00","launched":null,"completed":null,"progress":null,"completionStatus":null,"messages":[],"returns":[{"displayLabel":"TASK_OUT_ACCOUNT_GROUP_AGGREGATION_APPLICATIONS","attributeName":"applications"},{"displayLabel":"TASK_OUT_ACCOUNT_GROUP_AGGREGATION_TOTAL","attributeName":"total"},{"displayLabel":"TASK_OUT_ACCOUNT_GROUP_AGGREGATION_CREATED","attributeName":"groupsCreated"},{"displayLabel":"TASK_OUT_ACCOUNT_GROUP_AGGREGATION_UPDATED","attributeName":"groupsUpdated"},{"displayLabel":"TASK_OUT_ACCOUNT_GROUP_AGGREGATION_DELETED","attributeName":"groupsDeleted"}],"attributes":{"creatorRequestId":"994096fc72d94e5fb887fff317f95068","creatorStack":"tpe","taskStartDelay":null,"qpocJobId":"d80cbd92-d638-4057-a528-d9d7d47719a3","creatorSpanId":"e89f44eaeaa24fb991c0f00e14f12410"}}
	File [f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv]: Aggregated successfully.
Complete.
------------------------------------------------------------------------------------------------------------
 Elapsed time:       1 seconds                      
 Files processed:    1                              
------------------------------------------------------------------------------------------------------------
 Success:            1                              
	/Users/neil.mcglennon/test/resources/entitlements/f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv
------------------------------------------------------------------------------------------------------------
 Error:              0                              
------------------------------------------------------------------------------------------------------------
 Skipped:            0                              
------------------------------------------------------------------------------------------------------------
```

# Additional Details

This section outlines additional details related to troubleshooting, frequently asked questions, and other technical details.

### File Naming Convention

The File Upload Utility analyzes at the files or directories you specify, and then looks for the application’s source name or ID in the beginning of the file name of the file. For example, for a file named `2c918087701c40cf01701dfdf2c61e2a-AuthEmployees.csv` or `AuthEmployees.csv`, a source ID `2c918087701c40cf01701dfdf2c61e2a` would be found and returned.  A source name or ID is required for either account or entitlement aggregation to succeed as that is what the REST APIs require. To find the source ID, you can see this in the browser's URL in your browser, or via source REST APIs.

If File Upload Utility doesn't find the source name or ID in the file name, then it will skip the file and move on to the next. Everything will be logged as output, so you are able to determine which file(s) were processed or not.  To test and see if your source ID can be found, run the simple regular expression on the file name:

```regexp
^(\s)?([0-9a-f]{32})
```

### File Naming Backward Compatibility with Older Source IDs

For those who are upgrading from previous versions of File Upload Utility, you'll know that older source IDs (e.g. `184744`) are much shorter than newer source IDs (e.g. `2c918087701c40cf01701dfdf2c61e2a`).  In an attempt to keep backwards compatibility, if File Upload Utility detects an older short source ID (e.g. `184744`), it will attempt to look up which new source ID this might map to.  In order to do this, File Upload Utility will iterate through all sources in the system and download a list.  This is visible when running File Upload Utility with the `--verbose` option:

```shell
...
Analyzing directory: /Users/neil.mcglennon/test/resources
Analyzing account file: 184744-AuthEmployees.csv
        File [184744-AuthEmployees.csv]: Detected with older source ID reference [184744]. Attempting to resolve.
------------------------------------------------------------------------------------------------------------
 Building source file lookup table. Starting source iteration. 
 To avoid this scan, please switch to new Source IDs in your file names.
 Older Source ID references will not be used in the future.
------------------------------------------------------------------------------------------------------------
 184744     : 2c918087701c40cf01701dfdf2c61e2a      
 226454     : 2c9180877a3f5acc017a43c671103c03      
 228616     : 2c9180877c337e84017c5261e23f1ec4      
 236466     : 2c918087825f9081018265298ff14eee      
 237117     : 2c91808783c7d7fd0183c882da42115d      
 237118     : 2c91808783c7d8070183c88371a3112a      
 215594     : 2c9180887769630b01776ee9be6f587d      
 237091     : 2c918088838b1cce0183c2b71ef23f05      
 228795     : 2c9180897cb94778017cc3a4af3e3d0e        
------------------------------------------------------------------------------------------------------------
        File [184744-AuthEmployees.csv]: Successfully resolved old source ID reference [184744] to new source ID reference [2c918087701c40cf01701dfdf2c61e2a]
        File [184744-AuthEmployees.csv]: Submitting Account Aggregation: Source ID[2c918087701c40cf01701dfdf2c61e2a], Disable Optimization[false]
        File [184744-AuthEmployees.csv]: Aggregation response: {"success":true,"task":{"type":"QUARTZ","id":"af1b82086c194f89acce82e156ff6e61","name":"Cloud Account Aggregation","description":"Aggregates from the specified application.","parentName":null,"launcher":"System","created":1714574212775,"launched":1714574212788,"completed":null,"completionStatus":null,"messages":[],"returns":[{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_APPLICATIONS","attributeName":"applications"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_TOTAL","attributeName":"total"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_OPTIMIZED","attributeName":"optimizedAggregation"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_IGNORED","attributeName":"ignored"},{"displayLabel":"TASK_OUT_UNCHANGED_ACCOUNTS","attributeName":"optimized"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_CREATED","attributeName":"created"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_UPDATED","attributeName":"updated"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_DELETED","attributeName":"deleted"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_MANAGER_CHANGES","attributeName":"managerChanges"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_BUSINESS_ROLE_CHANGES","attributeName":"detectedRoleChanges"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_EXCEPTION_CHANGES","attributeName":"exceptionChanges"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_POLICIES","attributeName":"policies"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_POLICY_VIOLATIONS","attributeName":"policyViolations"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_POLICY_NOTIFICATIONS","attributeName":"policyNotifications"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_SCORES_CHANGED","attributeName":"scoresChanged"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_SNAPSHOTS_CREATED","attributeName":"snapshotsCreated"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_SCOPES_CREATED","attributeName":"scopesCreated"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_SCOPES_CORRELATED","attributeName":"scopesCorrelated"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_SCOPES_SELECTED","attributeName":"scopesSelected"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_SCOPES_DORMANT","attributeName":"scopesDormant"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_UNSCOPED_IDENTITIES","attributeName":"unscopedIdentities"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_CERTIFICATIONS_CREATED","attributeName":"certificationsCreated"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_CERTIFICATIONS_DELETED","attributeName":"certificationsDeleted"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_APPLICATIONS_GENERATED","attributeName":"applicationsGenerated"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_MANAGED_ATTRIBUTES_PROMOTED","attributeName":"managedAttributesCreated"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_MANAGED_ATTRIBUTES_PROMOTED_BY_APP","attributeName":"managedAttributesCreatedByApplication"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_IDENTITYENTITLEMENTS_CREATED","attributeName":"identityEntitlementsCreated"},{"displayLabel":"TASK_OUT_ACCOUNT_AGGREGATION_GROUPS_CREATED","attributeName":"groupsCreated"}],"attributes":{"eventId":4311625,"appId":"2c918087701c40cf01701dfdf2c61e2a","optimizedAggregation":"enabled"},"progress":null}}
        File [184744-AuthEmployees.csv]: Aggregated successfully.
```

For files that are not able to resolve, with the `--verbose` option enabled, you should see messages like this:

```shell
Analyzing account file: 82343-accounts.csv
        File [82343-accounts.csv]: Detected with older source ID reference [82343]. Attempting to resolve.
        File [82343-accounts.csv]: Unable to resolve old source ID reference [82343] to new source ID reference. This file will be skipped.
        File [82343-accounts.csv]: Does not contain a valid source ID. Skipping...
```

This source file lookup table is generated once per execution of File Upload Utility and kept in memory.  For organizations with a lot of sources, this might be somewhat expensive, and sources going forward might not have references to old source IDs going forward.  To prevent this iteration, rename the files with the more modern file namings standard as mentioned in **File Naming Convention**.

### Multiple Files or Directories

File Upload Utility does allow specifying multiple files or directories, by supplying the `--file` or `-f` options multiple times.

Here is example usage:
```shell
$ java -jar sailpoint-file-upload-utility.jar --url https://example.api.identitynow.com --clientId d0b28ce1b2694b64949dd546de1ff574 --clientSecret a34...1df --file /Users/neil.mcglennon/test/file1/ --file /Users/neil.mcglennon/test/file2/
```

### Environment Variable Configurations

File Upload Utility does allow usage of environmental variables for the client ID and secret configurations.  By providing option `--clientId env` the client ID will be sourced from the `SAIL_CLIENT_ID` environment variable. By providing option `--clientSecret env` the client secret will be sourced from the `SAIL_CLIENT_SECRET` environment variable. 

```shell
$ java -jar sailpoint-file-upload-utility.jar --url https://example.api.identitynow.com --clientId env --clientSecret env --file /Users/neil.mcglennon/test/resources/ -R
------------------------------------------------------------------------------------------------------------
 SailPoint File Upload Utility
------------------------------------------------------------------------------------------------------------
 Version:            4.1.1                          
 Date:               2024-09-10 16:15 CST           
 Docs:               https://developer.sailpoint.com/discuss/t/file-upload-utility/18181 
------------------------------------------------------------------------------------------------------------
 --clientId derived from $SAIL_CLIENT_ID
 --clientSecret derived from $SAIL_CLIENT_SECRET
 URL:                https://example.api.identitynow.com 
 Client ID:          d0b28ce1b2694b64949dd546dd1ff574 
 Files:              /Users/neil.mcglennon/test/resources
 ObjectType:         account                        
 Optimization:       true                           
 Recursive:          true                           
 Extensions:         csv                            
 Simulation:         false                          
 Verbose:            true                           
 Timeout:            10000                          
------------------------------------------------------------------------------------------------------------
Checking credentials...
Analyzing directory: /Users/neil.mcglennon/test/resources
Analyzing account file: 184744-AuthEmployees.csv
...
```

### Interactive Password Entry

File Upload Utility does allow secrets to be entered in an interactive fashion - for the `--clientSecret` and the `--proxyPassword`.

```shell
$ java -jar sailpoint-file-upload-utility.jar --url https://example.api.identitynow.com --clientId d0b28ce1b2694b64949dd546dd1ff574 --clientSecret --file /Users/neil.mcglennon/test/resources/ -R
Enter value for --clientSecret (SailPoint Client Secret (PAT)): a34xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1df
------------------------------------------------------------------------------------------------------------
 SailPoint File Upload Utility
------------------------------------------------------------------------------------------------------------
 Version:            4.1.1                          
 Date:               2024-09-10 16:15 CST           
 Docs:               https://developer.sailpoint.com/discuss/t/file-upload-utility/18181 
------------------------------------------------------------------------------------------------------------
 URL:                https://neil-test.api.identitynow.com 
 Client ID:          d0b28ce1b2694b64949dd546dd1ff574 
 Files:              /Users/neil.mcglennon/test/resources 
 ObjectType:         account                        
 Optimization:       true                           
 Recursive:          true                           
 Extensions:         csv                            
 Simulation:         false                          
 Verbose:            true                           
 Timeout:            10000                          
------------------------------------------------------------------------------------------------------------
Analyzing directory: /Users/neil.mcglennon/test/resources
Analyzing account file: 184744-AuthEmployees.csv
...
```

### Proxy Configurations

Web (HTTP) proxies are supported by supplying proxy settings with the `--proxyHost` and `--proxyPort` options.  Here is example usage:
```shell
$ java -jar sailpoint-file-upload-utility.jar --url https://example.api.identitynow.com --clientId d0b28ce1b2694b64949dd546de1ff574 --clientSecret a34...1df --file /Users/neil.mcglennon/test/resources/ --proxyHost 192.168.10.1 --proxyPort 8080
```

For authenticated proxies, add `--proxyUser` and `--proxyPassword` options as well. Here is example usage:
```shell
$ java -jar sailpoint-file-upload-utility.jar --url https://example.api.identitynow.com --clientId d0b28ce1b2694b64949dd546de1ff574 --clientSecret a34...1df --file /Users/neil.mcglennon/test/resources/ --proxyHost 192.168.10.1 --proxyPort 8080 -proxyUser MyProxyUser --proxyPassword pr0xyP@s$w0rD
```

### Timeout Configurations

Timeout settings are configurable by supplying a number of milliseconds to configure the timeout, using the `--timeout`, or `-t` options respectively. The default timeout in the File Upload Utility is 10 seconds (10,000 ms).

```shell
$ java -jar sailpoint-file-upload-utility.jar --url https://example.api.identitynow.com --clientId d0b28ce1b2694b64949dd546de1ff574 --clientSecret a34...1df --file /Users/neil.mcglennon/test/resources/ --timeout 15000
```

Often timeouts are an indication that File Upload Utility is attempting to communicate with the SailPoint Cloud and not getting a response.  Usually, this due to network security controls, such as firewalls, preventing the communication.  Work with your network teams to make sure you can reach the SailPoint Cloud, and if necessary, adjust **Timeout Configurations** or even leverage **Proxy Configurations** if so required. 


## Troubleshooting

### Timeouts
Timeouts are usually an indication that File Upload Utility is attempting to communicate with the SailPoint Cloud and not getting a response.  Usually, this due to network security controls, such as firewalls, preventing the communication.  Work with your network teams to make sure you can reach the SailPoint Cloud, and if necessary, adjust **Timeout Configurations** or even leverage **Proxy Configurations** if so required.

See also **Timeout Configurations**.

### 401 (Unauthorized) Errors 

When File Upload Utility first connects to the SailPoint Cloud, it checks credentials to make sure the supplied Client ID and Client Secret are valid, and provide an access token.  If this fails, you'll typically see an error that looks like this:
```text
Checking credentials...
Error Logging into Identity Security Cloud.  Please check your credentials and try again. [Error authenticating with credentials: 401 ]
```
In order to remedy this, make sure you are using a correct Personal Access Token on the right SailPoint tenant and URL. 

### 403 (Forbidden) Aggregation Errors

When File Upload Utility first connects to the SailPoint Cloud, it checks credentials to make sure the supplied Client ID and Client Secret are valid, and provide an access token.  While an access token may be valid, the token's usage may not have adequate access rights in order to carry out account or entitlement aggregations.  Make sure that the Personal Access Token supplied has correct access rights needed.  At current time of writing `ORG_ADMIN`, `SOURCE_ADMIN` or `SUB_SOURCE_ADMIN` access rights are required.

### 400 (Bad Request) Aggregation Errors

File Upload Utility doesn't perform any sort of validation or manipulation of files; it merely acts as transport to the SailPoint Cloud via the account aggregation and entitlement aggregation REST APIs.  Once the files are received, the SailPoint Cloud can provide errors back vis-a-vis the REST API responses.  These generally look like these:
```text
Analyzing account file: f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv
	File [f1f3b747be924745afbc0c8a53f71baf-file-test-entitlement-feed.csv]: Error: HTTP Code[400] Message[] Body[{"detailCode":"400.1.3 Illegal value","trackingId":"44a4f5a299d540df84537fc51fd9271f","messages":[{"locale":"und","localeOrigin":"REQUEST","text":"Column: \"[displayName, created, description, modified, entitlements, permissions]\" is unknown and/or column: \"[givenName, familyName, e-mail, location, manager]\" is missing"},{"locale":"en-US","localeOrigin":"DEFAULT","text":"Column: \"[displayName, created, description, modified, entitlements, permissions]\" is unknown and/or column: \"[givenName, familyName, e-mail, location, manager]\" is missing"}],"causes":[]}]
```

If any sort of 400 Bad Request errors are given, check your file configuration is relevant for the type of aggregation you are doing.  Sometimes this can be due to not specifying the correct object class on entitlement aggregations; this can be easily remedied by checking the `--objectType` option, and making sure the corresponding entitlement schema exists.

### 500 (Internal Server Error) Aggregation Errors

If you see a 500 Internal Server Error, this means there is a problem with the SailPoint Cloud processing itself.  If you see this, it is best to reproduce the issue using the REST APIs and alert SailPoint Support.  

## Frequently Asked Questions

- Q: Can I configure values with environment variables?
- A: By providing `--clientId env` the client ID will be sourced from the `SAIL_CLIENT_ID` environment variable. By providing `--clientSecret env` the client secret will be sourced from the `SAIL_CLIENT_SECRET` environment variable.


- Q: How can I secure my `--clientSecret` values?
- A: The `--clientSecret` value can be entered via arguments, and provided and secured by any other scripts calling File Upload Utility.  Additionally, File Upload Utility 4.1.1 supports interactive entry by providing `--clientSecret` with no value.  Client secrets can be provided from the `SAIL_CLIENT_SECRET` environment variable by configuring the command line option as `--clientSecret env`.


- Q: What are you doing about CC/V2 APIs being deprecated?
- A: As of File Upload Utility 4.0, this leverages supported V3 SailPoint REST APIs.  No older or private (CC) REST APIs are used in this.  


- Q: Does File Upload Utility still support older (CC) Source IDs?
- A: See **File Naming Backward Compatibility with Older Source IDs**


- Q: How is File Upload Utility’s transmission secured?
- A: File Upload Utility uses SailPoint REST APIs which are all secured over HTTPS / TLS 1.2.


- Q: Does File Upload Utility support file-level encryption?
- A: File Upload Utility does not currently support encryption or decryption of files themselves. However, additional scripting could surround File Upload Utility which could encrypt, decrypt, modify, or move files.


- Q: Does File Upload support reading from SFTP, FTP, or SSH locations?
- A: File Upload Utility only supports reading from local drives, not from remote SFTP, FTP, or SSH locations. However, additional scripting could surround File Upload Utility which could connect to SFTP, FTP, or SSH sessions to transmit, modify, or move files.


- Q: What REST APIs does this use?
- A: We use the following REST APIs:
  - Account Aggregation - For account aggregations
  - Entitlement Aggregation - For entitlement aggregations
  - List Accounts - For source lookup
  - Get Account - For source lookup

## Upgrading from Previous Versions

If you are coming from previous versions of File Upload Utility, you'll notice a few important changes:

- **Java Requirements** - File Upload Utility 4.1.1 requires Java JDK 11+
- **Command Line Options** - Some command line options have changed.  See **Options** for details.
- **SailPoint APIs** - We are no longer using older (deprecated) CC Source APIs, and instead use newer Source APIs for Account Aggregation and Entitlement Aggregation.
- **Source ID Format** - We use newer source ID  (e.g. `2c918087701c40cf01701dfdf2c61e2a`) formats in file names.  If you have older source ID (e.g. `184744`) formats we'll attempt to look it up.  See **File Naming Backward Compatibility with Older Source IDs**
- **Proxy Type** - For those using proxies, proxy type no longer needs to be given.

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag `enhancement`.
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<!-- CONTACT -->
## Discuss
[Click Here](https://developer.sailpoint.com/discuss/new-topic?title=Your%20CoLab%20question%20title&body=Your%20CoLab%20question%20body%20here&category_id=2&tags=colab) to discuss the Colab with other users.
