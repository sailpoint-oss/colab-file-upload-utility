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
The SailPoint File Upload Utility provides a way for SailPoint customers to upload files to Identity Security Cloud automatically, for automatic account and entitlement aggregations. This is an executable Java-based JAR which can be executed with a set of options, or scheduled via CLI utilities like cron. This utility uploads the files the same way as if you were to upload the files via the admin user interface, by calling SailPoint REST APIs.

# Requirements
This utility is a Java based application and requires Java Development Kit (JDK) 17 or higher to run.  We build and test against OpenJDK 17.

This also requires external, outbound access over HTTPS (443) for REST HTTP API calls to the SailPoint Cloud.

# Guide

## Frequently Asked Questions

- Q: What are you doing about CC/V2 APIs being deprecated?
- Good news! See my full response here:


- Q: How is File Upload Utility’s transmission secured?
- A: File Upload Utility uses SailPoint REST APIs which are all secured over HTTPS / TLS 1.2.

- Q: Does File Upload Utility support file-level encryption?
- A: File Upload Utility does not currently support encryption or decryption of files themselves. However, additional scripting could surround File Upload Utility which could encrypt, decrypt, modify, or move files.

- Q: Does File Upload support reading from SFTP, FTP, or SSH locations?
- A: File Upload Utility only supports reading from local drives, not from remote SFTP, FTP, or SSH locations. However additional scripting could surround File Upload Utility which could connect to SFTP, FTP, or SSH sessions to transmit, modify, or move files.

## File Upload / Aggregation

The core purpose of the file upload utility is to upload one or many files from a specified directory (or directories), and send them to the SailPoint cloud for aggregation. To use this, you will provide the following options:

| Option | Required	| Description
| ---|
|-url <arg>	| Yes | The SailPoint API Gateway. e.g. https://tenant.api.identitynow.com

-client_id <arg>	Yes	Personal Access Token Client ID.
For more information, see: Best Practices: Using Personal Access Tokens in IdentityNow 8.
-client_secret <arg>	Yes	Personal Access Token Client Secret.
For more information, see: Best Practices: Using Personal Access Tokens in IdentityNow 8.
-f <arg>, -files <arg>	Yes (if -e <arg>, -entitlement <arg> does not exist)	The specific files or directories where to find files to aggregate accounts. This can be specified multiple times.
-disableOptimization	No	Disables optimization for account aggregation.
Default is false.
-e <arg>, -entitlement <arg>	Yes (if -f <arg>, -files <arg> does not exist)	The specific files or directories where to find files to aggregate entitlements. This can be specified multiple times.
-type	No	Optional Parameter to specify “type” of an entitlement aggregation.
Default is group.
-r, -R	No	Optional flag to recursively search subdirectories.
Default is false.
-s, -S, -simulate	No	Optional flag to simulate reading / processing of the files, but perform no actual uploading or aggregation.
Default is false.
-t <arg>, -timeout <arg>	No	Optional value for timeout settings. The value is the number of milliseconds the timeout is configured for.
Default is 10,000ms (10 seconds).
-x <arg>, -extension <arg>	No	Optional value to specify file extensions to look for when traversing directories. This can be specified multiple times.
Default is csv.
-v, -verbose	No	Optional flag to enable verbose logging.
Default is false.
-proxy_host	No	Optional proxy host name. Use -proxyHost, -proxyPort, and -proxyType together.
-proxy_port	No	Optional proxy port number. Use -proxyHost, -proxyPort, and -proxyType together.
-proxy_type	No	Optional proxy type. Use -proxyHost, -proxyPort, and -proxyType together. Values can be: http, socks, direct. Default is http.
-proxy_user	No	Optional proxy username for use in proxy authentication.
-proxy_password	No	Optional proxy password for use in proxy authentication.

## File Naming Convention

The File Upload Utility analyzes at the files or directories you specify, and then looks for the application’s source ID in the file name of the file. For example, for a file named “14586 - hr_users.csv” would determine that this file belongs to source ID 14586. A source ID is required for aggregation.

If it doesn’t find the source ID in the file name, then it will skip the file and move on. Everything will be logged as output, so you are able to determine the file which are processed or not. The file upload utility currently uses private APIs to upload a file to a source, and the private APIs use a short ID to identify sources.
Previously, you could find the corresponding source IDs by looking at the ID in the URL of the application source in the IdentityNow user interface. However, the UI now uses the long ID for sources. To get the short ID for the source you want to upload a file to, you must use the following API endpoint:

https://<tenant>.api.identitynow.com/cc/api/source/list

To test and see if your source ID can be found, run the simple regular expression on the file name:

^(\\s)?([0-9]{1,10})
Multiple Files or Directories

File Upload Utility does allow specifying multiple files or directories, by supplying the -file or -f multiple times.

Here is example usage:

$java -jar FileUploadUtility.jar -url https://example.api.identitynow.com -client_id 550...b2a -client_secret 4fc...5e7 -f "/tmp/files/62863 - Employees.csv" -f "/tmp/files/62864 - Contractors.csv"
Multiple Extensions

When directories are specified, the File Upload Utility looks only at files with certain file extensions. By default these are files with an extension of .csv. To override these default extensions, you can specify multiple -x or -extension options.

Here is example usage:

$java -jar FileUploadUtility.jar -url https://example.api.identitynow.com -client_id 550...b2a -client_secret 4fc...5e7 -f /tmp/files/ -x txt -x csv
Proxy Support

Web-proxies are supported by supplying a proxy host, port, and type with the -proxy_host, -proxy_port, and -proxy_type options respectively.

Here is example usage:

$java -jar FileUploadUtility.jar -url https://example.api.identitynow.com -client_id 550...b2a -client_secret 4fc...5e7 -f /tmp/files/ -proxy_host 192.168.10.1 -proxy_port 8080 -proxy_type http
The proxy support supports Basic Authentication as well by supplying the -proxy_user and -proxy_password values as well.

Here is example usage:

$java -jar FileUploadUtility.jar -url https://example.api.identitynow.com -client_id 550...b2a -client_secret 4fc...5e7 -f /tmp/files/ -proxy_host 192.168.10.1 -proxy_port 8080 -proxy_type http -proxy_user admin -proxy_password SomeAdminPassword
Timeout Configuration

Ttimeout settings are configurable by supplying a number of milliseconds to configure the timeout, using the -timeout, or -t options respectively. The default timeout in the file upload utility is 10 seconds (10,000 ms).

Here is example usage:

$java -jar FileUploadUtility.jar -url https://example.api.identitynow.com -client_id 550...b2a -client_secret 4fc...5e7 -f /tmp/files/ -timeout 15000
Usage Example

Uploading Accounts via file upload

$java -jar FileUploadUtility.jar -url https://example.api.identitynow.com -client_id 550...b2a -client_secret 4fc...5e7 -f /tmp/files/ -disableOptimization -R -v
--------------------------------------------------------------
SailPoint IdentityNow File Upload Utility
--------------------------------------------------------------
Version:	3.0.5
Date:		2022-12-01 15:00 CST
Docs:		https://community.sailpoint.com/docs/DOC-3140
--------------------------------------------------------------
--------------------------------------------------------------
URL:		        https://example.api.identitynow.com
Account Files:	        /tmp/files
Optimization:          Disabled
Recursive:	        true
Extensions:            csv
Simulation:	        false
Verbose:               true
Timeout:               10000
--------------------------------------------------------------
Checking credentials...
Analyzing directory: /tmp/files
Analyzing Account file:  62863.csv
File [ 62863.csv] was aggregated successfully.
Analyzing file: 62863 - Employees.csv
File [62863 - Employees.csv] was aggregated successfully.
Complete.
--------------------------------------------------------------
Elapsed time:		2 seconds
Total files processed:	2
--------------------------------------------------------------
Success:	2
/tmp/files/62863.csv
/tmp/files/62863 - Employees.csv
--------------------------------------------------------------
Error:		0
--------------------------------------------------------------
Skipped:	0
--------------------------------------------------------------

Uploading Entitlements via file upload

$java -jar FileUploadUtility.jar -url https://example.api.identitynow.com -client_id 550...b2a -client_secret 4fc...5e7 -e /tmp/files/81260 - entitlement.csv
--------------------------------------------------------------
SailPoint IdentityNow File Upload Utility
--------------------------------------------------------------
Version:	3.0.5
Date:		2022-12-01 15:00 CST
Docs:		https://community.sailpoint.com/docs/DOC-3140
--------------------------------------------------------------
--------------------------------------------------------------
URL:			https://example.api.identitynow.com
Entitlement Files:	/tmp/files/81260 - entitlement.csv
Entitlement Type:	group
Recursive:		false
Extensions:		csv
Simulation:		false
Verbose:		false
Timeout:		10000
--------------------------------------------------------------
Checking credentials...
Analyzing Entitlement file: /tmp/files/81260 - entitlement.csv
Analyzing Entitlement file: 81260 - entitlement.csv
Entitlement File [81260 - entitlement.csv] was aggregated successfully.
Complete.
--------------------------------------------------------------
Elapsed time:		2 seconds
Total files processed:	1
--------------------------------------------------------------
Success:	1
/tmp/files/81260 - entitlement.csv
--------------------------------------------------------------
Error:		0
--------------------------------------------------------------
Skipped:	0
--------------------------------------------------------------

## Help

The about command displays information about the IdentityNow File Upload Utility system version and date. This may be useful for troubleshooting and debugging.

Option	Required	Description
-h,-help,-?	Yes	Displays usage information.
Usage Example

```
$ java -jar sailpoint-file-upload-utility.jar --help

Usage:

Perform bulk file aggregations to Identity Security Cloud.

java -jar sailpoint-file-upload-utility.jar [-dhRSvV] -s[=<clientSecret>]
[-H=<proxyHost>] -i=<clientId> [-o=<objectType>] [-P=<proxyPort>]
[-t=<timeout>] -u=<url> [-U=<proxyUser>] [-W=<proxyPassword>] -f=<files>
[-f=<files>]... [-x=<fileExtensions>]...

Description:

Scans specified files and directories for files in bulk, to send to Identity
Security Cloud for account or entitlement aggregation.  For more details see:
https://developer.sailpoint.com/discuss/t/file-upload-utility/18181

Options:
  -u, --url=<url>           SailPoint API Gateway (e.g. https://tenant.api.
                              identitynow.com)
  -i, --client_id=<clientId>
                            SailPoint Client ID (PAT)
  -s, --client_secret[=<clientSecret>]
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
  -H, --proxy_host=<proxyHost>
                            Proxy Host
  -P, --proxy_port=<proxyPort>
                            Proxy Post
  -U, --proxy_user=<proxyUser>
                            Proxy User; Used for authenticated proxies
  -W, --proxy_password=<proxyPassword>
                            Proxy Password; Used for authenticated proxies
  -V, --version             Displays the current version.
  -h, --help                Display help.
```




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
