# Configuration File Format
The `SailPoint File Upload Utility` now supports configuration using a JSON file to reduce the complexity of the configuration and command line arguments and supports additional features.

Below is an example of a configuration file:
```json
{
  "tenant": {
    "url": "https://<example>.api.identitynow.com",
    "clientId": "env",
    "clientSecret": "env"
  },
  "proxy": {
    "enabled": false,
    "host": "",
    "port": 1234,
    "user": "",
    "password": ""
  },
  "aggregations": [
    {
      "sourceId": "<example source id>>",
      "disableOptimization": false,
      "objectType": "Account",
      "recursive": false,
      "simulate": false,
      "timeout": 10000,
      "extensions": [
        "csv",
        "tsv"
      ],
      "enableFileJourney": true,
      "structure": {
        "in": "/data/sailpoint/in",
        "stage": "/data/sailpoint/stage",
        "archive": "/data/sailpoint/archive",
        "error": "/data/sailpoint/error"
      }
    }
  ],
  "ks": "",
  "iv": ""
}
```

Most of the values in the config file directly relate to the command line arguments outlined in the [README.md](./README.md) organized more efficiently with some additional features outlined below

## Attributes
The following attributes and objects are required to be present in the configuration file.  If there is no value for a specific attribute, the blank `""` is accepted and `[]` for arrays:
|Object|Name|Type|Description|
|---|---|---|---|
||`tenant`|`Tenant`|Object to outline the details of the SailPoint tenant to connect to.|
|`Tenant`|`url`|`String`|Contains the details of the SailPoint tenant to connect to.|
|`Tenant`|`clientId`|`String`|SailPoint Client ID (PAT).  If value of `env` is provided, then the value for environment variable `SAIL_CLIENT_ID` will be used.|
|`Tenant`|`clientSecret`|`String`|SailPoint Client Secret (PAT).  If value of `env` is provided, then the value for environment variable `SAIL_CLIENT_SECRET` will be used.  Note: Plain text `clientId` is automatically encrypted on first run.|
||`aggregations`|`Aggregation (Array)`|Array of `Aggregation` objects which contain the configuration for each source o be aggregated.|
|`Aggregation`|`sourceId`|`String`|SailPoint ID for the Source to be aggregated.  If File Upload Utility cannot find the specified `sourceId` in the tenant's configuration, then it will skip the aggregation and move on to the next if there is one.|
|`Aggregation`|`objectType`|`String`|`Account` or `Entitlement` Schema|
|`Aggregation`|`recursive`|`Boolean`|Recursively search directories|
|`Aggregation`|`timeout`|`Integer`|Timeout (in milliseconds). Default: 10000 (10s)|
|`Aggregation`|`disableOptimization`|`Boolean`|Disable Optimization on Account Aggregation|
|`Aggregation`|`simulate`|`Boolean`|Simulation Mode.  Scans for files but does not aggregate.|
|`Aggregation`|`extensions`|`String (Array)`|List of extensions to search for within the directory structure.|
|`Aggregation`|`enableFileJourney`|`Boolean`|Flag to enable file processing to move files to `in`, `staging`, `archive` or `error`|
|`Aggregation`|`structure`|`Structure`|Directory structure for importing files|
|`Structure`|`in`|`String`|Path to file or directory for aggregation|
|`Structure`|`stage`|`String`|Path to directory for processing aggregations|
|`Structure`|`archive`|`String`|Path to directory for successful aggregations|
|`Structure`|`error`|`String`|Path to directory for failed aggregations|
||`proxy`|`Proxy`|Object to outline the details of a proxy server (if required).|
|`Proxy`|`enabled`|`Boolean`|Flag to enable Web (HTTP) proxies support.|
|`Proxy`|`host`|`String`|Hostname for proxy server (if `enabled` is set to `true`).|
|`Proxy`|`port`|`Integer`|Proxy TCP port for proxy server (if `enabled` is set to `true`).|
|`Proxy`|`user`|`String`|Username for proxy server (if `enabled` is set to `true`).|
|`Proxy`|`password`|`String`|Password for proxy server (if `enabled` is set to `true`).  Note: Plain text `clientId` is automatically encrypted on first run.|
||`ks`|`String`|Internally used.  Soon to be removed.|
||`iv`|`String`|Internally used.  Soon to be removed.|

## New Features
The following new features have been introduced as part of the configuration file:

### One File Config
One single file for configuration, reducing the reliance on command line arguments, helps the maintenance of the application.

### Individual Source Aggregation Configuration
Previously, the following settings could only be set as globally for the application's run (regardless of the files and sources being aggregated):
- timeout
- disableOptimization
- extensions
- simulate

As of now each of these options can be configured per-source, enabling a more granular configuration:

```json
{
  ...
  "aggregations": [
    {
      "sourceId": "<<<source id>>>",
      "disableOptimization": false,
      "objectType": "Account",
      "recursive": false,
      "simulate": false,
      "timeout": 10000,
      "extensions": [
        "csv",
        "tsv"
      ],
      "enableFileJourney": true,
      "structure": {
        "in": "/data/projects/sailpoint/in",
        "stage": "/data/projects/sailpoint/stage",
        "archive": "/data/projects/sailpoint/archive",
        "error": "/data/projects/sailpoint/error"
      }
    }
  ],
  ...
}
```

### File Archival / File Journey - Built in
In many cases, files are picked up from a directory and processed, and there is often a reliance of a separate or overarching script of some sort (`bash`/`PowerShell` for example) which either picks the file up for processing by this application, or moves the file to an archival directory.

This has been addressed with the `structure` object within the configuration:

```json
{
  ...
  "aggregations": [
    {
      "sourceId": "<example source id>>",
      ...
      "extensions": [
        "csv",
        "tsv"
      ],
      "enableFileJourney": true,
      "structure": {
        "in": "/data/sailpoint/in",
        "stage": "/data/sailpoint/stage",
        "archive": "/data/sailpoint/archive",
        "error": "/data/sailpoint/error"
      }
    }
  ],
  ...
}
```

As can be seen above, if the `enableFileJourney` flag is set to `true` the following happens during processing:
1) The directory specified in the `in` attribute is scanned for files with extensions which match one of the `extensions` also specified.
2) Upon finding a file, the file is renamed with a prefix of the date and time e.g. `20250705010203_<filename>`.
3) The file `20250705010203_<filename>` is then moved to the diectory specified in the `stage` attribute
so to clean the `in` directory.
4) The file is then attempted to be imported / aggregated.
5) If the file is successfully aggregated, the file is moved in to the directory specified in the `archive` folder.
6) If the aggregation of the file fails for any reason, the file is moved in to the directory specified in the `error` attribute.

If the `enableFileJourney` flag is set to `false` the following happens during processing:
1) The directory specified in the `in` attribute is scanned for files with extensions which match one of the `extensions` also specified.
2) The file is then attempted to be imported / aggregated.

### On-the-fly Encryption of Credentials
As per the original codebase, the environment variables for the tenant Client ID and Secret can be used.  However if this is not viable, the Client Secret can be entered **Free Text** in to the configuration file.

However, upon the first run on the application, the secret will be encrypted and no longer be human readable.

Note: Currently this is not a fool-proof encryption method as the keys are stored in the config file, this is only a deterrent at this time.  A more suitable alternative is currently being looked in to.

