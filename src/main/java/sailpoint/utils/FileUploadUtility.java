package sailpoint.utils;

import okhttp3.ResponseBody;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import sailpoint.object.Source;
import sailpoint.service.SailPointService;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sailpoint.utils.FileUploadUtility.*;

@CommandLine.Command(
		usageHelpAutoWidth = true,
		name = "java -jar sailpoint-file-upload-utility.jar",
		sortOptions = false,
		headerHeading = "%nUsage:%n%n",
		synopsisHeading = "%n",
		descriptionHeading = "%nDescription:%n%n",
		parameterListHeading = "%nParameters:%n",
		optionListHeading = "%nOptions:%n",
		header = "Perform bulk file aggregations to Identity Security Cloud.",
		description = "Scans specified files and directories for files in bulk, to send to Identity Security Cloud for account or entitlement aggregation.  For more details see: " + ABOUT_LINK,
		version = {
		"SailPoint File Upload Utility " + ABOUT_VERSION,
		"Build: " + ABOUT_DATE,
		"Documentation: " + ABOUT_LINK,
		"JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
		"OS: ${os.name} ${os.version} ${os.arch}"})
public class FileUploadUtility implements Callable<Integer> {

	/**
	 * Metadata about the File Upload Utility
	 */
	public static final String ABOUT_DATE = "2024-09-10 16:15 CST";
	public static final String ABOUT_VERSION = "4.1.1";
	public static final String ABOUT_LINK = "https://developer.sailpoint.com/discuss/t/file-upload-utility/18181";

	/**
	 * Command Line Parameters
	 */
	@Option( names = { "-u", "--url" }, required = true, description = "SailPoint API Gateway (e.g. https://tenant.api.identitynow.com)" )
	private String url = "";

	@Option( names = { "-i", "--clientId" }, required = true, description = "SailPoint Client ID (PAT)" )
	private String clientId = "";

	@Option( names = { "-s", "--clientSecret" }, description = "SailPoint Client Secret (PAT)", arity = "0..1", interactive = true )
	private String clientSecret = "";

	@Option( names = { "-f", "--file" }, required = true, description = "File or directories for bulk aggregation." )
	private List<File> files = null;

	@Option( names = { "-d", "--disableOptimization" }, description = "Disable Optimization on Account Aggregation" )
	private boolean disableOptimization = false;

	@Option( names = { "-o", "--objectType" }, description = "File Type; Account or Entitlement Schema. Default: Account" )
	private String objectType = DEFAULT_ACCOUNT_AGGREGATION;

	@Option( names = { "-R", "--recursive" }, description = "Recursively search directories" )
	private boolean recursive = false;

	@Option( names = { "-S", "--simulate" }, description = "Simulation Mode.  Scans for files but does not aggregate." )
	private boolean simulate = false;

	@Option( names = { "-t", "--timeout" }, description = "Timeout (in milliseconds). Default: 10000 (10s)" )
	private Integer timeout = 10000;  // Default is 10s

	@Option( names = { "-x", "--extension" }, description = "File extensions to search (for directories only).  Default: csv" )
	private List<String> fileExtensions = Arrays.asList( "csv" );

	@Option( names = { "-v", "--verbose" }, description = "Verbose logging. Default: false" )
	private boolean verbose = false;

	@Option( names = { "-H", "--proxyHost" }, description = "Proxy Host" )
	private String proxyHost;

	@Option( names = { "-P", "--proxyPort" }, description = "Proxy Post" )
	private int proxyPort = -1;

	@Option( names = { "-U", "--proxyUser" }, description = "Proxy User; Used for authenticated proxies" )
	private String proxyUser = null;

	@Option( names = { "-W", "--proxyPassword" }, description = "Proxy Password; Used for authenticated proxies", arity = "0..1", interactive = true )
	private String proxyPassword = null;

	@Option(names = { "-V", "--version" }, versionHelp = true, description = "Displays the current version.")
	boolean versionRequested;

	@Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help.")
	boolean helpRequested;

	/**
	 * Variables for Execution
	 */
	private Logger logger;

	private Reporter reporter;

	private SailPointService sailPointService;

	/*
	 * This sourceReferenceMap is used to map old source IDs to new source IDs.
	 * Keyed by old source IDs; Values are new source IDs
	 * 184744 : 2c918087701c40cf01701dfdf2c61e2a
	 */
	private Map<String, String> sourceReferenceMap = null;

	public static final String DEFAULT_ACCOUNT_AGGREGATION = "account";

	public FileUploadUtility() {
		super();

		this.logger = new Logger();
		this.reporter = new Reporter();
	}
	
	public static void main( String[] args ) throws Exception {

		CommandLine commandLine = new CommandLine( new FileUploadUtility() );

		try {

			commandLine.parseArgs( args );

			FileUploadUtility fileUploadUtility = commandLine.populateCommand( new FileUploadUtility(), args );

			if( fileUploadUtility.helpRequested || args == null || args.length == 0 ) {
				commandLine.usage( System.out );
				return;
			}

			if( fileUploadUtility.versionRequested ) {
				commandLine.printVersionHelp( System.out );
				return;
			}

			System.exit( fileUploadUtility.call() );

		} catch ( UnsupportedClassVersionError ue ) {

			System.out.println( "Unsupported version of Java: Please upgrade to JDK 11 or higher." );
			System.exit( 1 );

		} catch ( RuntimeException rte ) {

			System.out.println( rte.getMessage() );
			System.exit( 1 );

		} catch ( Exception e ) {

			if( args == null || args.length == 0 )
				System.out.println( "No command arguments given.  Please provide arguments as indicated by the usage." );
			else
				System.out.println( "Invalid parameters given. Args [" + String.join( " ", args ) + "]." );

			commandLine.usage( System.out );
		}
	}

	@Override
	public Integer call() throws Exception {

		this.logger = new Logger( this.verbose );

		logger.info( "------------------------------------------------------------------------------------------------------------" );
		logger.info( " SailPoint File Upload Utility" );
		logger.info( "------------------------------------------------------------------------------------------------------------" );
		logger.info( String.format("%1$-20s %2$-30s ", " Version:", ABOUT_VERSION ) );
		logger.info( String.format("%1$-20s %2$-30s ", " Date:", ABOUT_DATE ) );
		logger.info( String.format("%1$-20s %2$-30s ", " Docs:", ABOUT_LINK ) );
		logger.info( "------------------------------------------------------------------------------------------------------------" );

		/*
		 * Perform some basic validations of the parameters provided.  Picocli already does validation of required parameters.
		 */
		if( StringUtils.endsWithIgnoreCase( clientId, "env" ) ) {
			logger.debug( " --clientId derived from $SAIL_CLIENT_ID" );
			clientId = System.getenv( "SAIL_CLIENT_ID" );
		}

		if( StringUtils.endsWithIgnoreCase( clientSecret, "env" ) ) {
			logger.debug( " --clientSecret derived from $SAIL_CLIENT_SECRET" );
			clientSecret = System.getenv( "SAIL_CLIENT_SECRET" );

		}

		if ( !StringUtils.startsWithIgnoreCase( url, "https://" ) )
			throw new RuntimeException( "Usage: The provided --url parameter must begin with 'https://'" );

		if ( !SailPointUrl.isValid( url ) )
			throw new RuntimeException( "Usage: The provided --url parameter must be a valid API URL: \n" + SailPointUrl.getDisplayableUrls() );

		/*
		 * Display configurations so that people can see how this is will run.  Also, useful for troubleshooting.
		 * We do not want to display the client secret here for security reasons.
		 */
		logger.info( String.format("%1$-20s %2$-30s ", " URL:", url ) );
		logger.info( String.format("%1$-20s %2$-30s ", " Client ID:", clientId ) );
		logger.info( String.format("%1$-20s %2$-30s ", " Files:", StringUtils.join( files, ", \n" ) ) );
		logger.info( String.format("%1$-20s %2$-30s ", " ObjectType:", objectType ) );

		if ( DEFAULT_ACCOUNT_AGGREGATION.equals( objectType ) )
			logger.info( String.format("%1$-20s %2$-30s ", " Optimization:", !disableOptimization ) );

		logger.info( String.format("%1$-20s %2$-30s ", " Recursive:", recursive ) );
		logger.info( String.format("%1$-20s %2$-30s ", " Extensions:", StringUtils.join( fileExtensions, "," ) ) );
		logger.info( String.format("%1$-20s %2$-30s ", " Simulation:", simulate ) );
		logger.info( String.format("%1$-20s %2$-30s ", " Verbose:", verbose ) );
		logger.info( String.format("%1$-20s %2$-30s ", " Timeout:", timeout ) );
		logger.info( "------------------------------------------------------------------------------------------------------------" );

		Timer.start();

		/*
		 * Create a SailPoint Service which will do all the API calls.
		 */
		this.sailPointService = new SailPointService.Builder()
				.url( url )
				.clientId( clientId )
				.clientSecret( clientSecret )
				.timeout( timeout )
				.proxy( proxyHost, proxyPort )
				.proxyAuthentication( proxyUser, proxyPassword )
				.build();

		if ( proxyHost != null && proxyPort != -1 )
			logger.debug( "Proxy enabled!  Initializing proxy with settings: proxyHost[" + proxyHost + "], proxyPort[" + proxyPort + "]." );

		/*
		 * Check to make sure credentials are valid before we process files.
		 */

		logger.info( "Checking credentials..." );

		try {

			sailPointService.createSession();

		} catch ( Exception e ) {
			logger.error( "Error Logging into Identity Security Cloud.  Please check your credentials and try again. [" + e.getMessage() + "]" );
			System.exit(1);
		}

		/*
		 * Start processing files...
		 */

		if ( CollectionUtils.isNotEmpty( files ) ) {

			for (File file : files ) {

				if ( file.isDirectory() ) {

					logger.info( "Analyzing directory: " + file );

					@SuppressWarnings("unchecked")
					Iterator<File> fileIterator = FileUtils.iterateFiles( file, fileExtensions.toArray(String[]::new), recursive );

					while ( fileIterator.hasNext() ) {
						processFile( fileIterator.next() );
					}

				} else {
					logger.info("Analyzing " + objectType + " file: " + file);
					processFile( file );
				}
			}
		}

		logger.info( "Complete." );

		logger.info( "------------------------------------------------------------------------------------------------------------" );
		logger.info( String.format("%1$-20s %2$-30s ", " Elapsed time:", ( Timer.secondsElapsed() ) + " seconds" ) );
		logger.info( String.format("%1$-20s %2$-30s ", " Files processed:", reporter.countTotal() ) );
		logger.info( "------------------------------------------------------------------------------------------------------------" );

		logger.info( String.format("%1$-20s %2$-30s ", " Success:", reporter.countSuccess() ) );
		for( String successFile : reporter.getSuccess() )
			logger.debug( "\t" + successFile );

		logger.info( "------------------------------------------------------------------------------------------------------------" );

		logger.info( String.format("%1$-20s %2$-30s ", " Error:", reporter.countErrors() ) );
		for( String errorFile : reporter.getErrors() )
			logger.debug( "\t" + errorFile );

		logger.info( "------------------------------------------------------------------------------------------------------------" );

		logger.info( String.format("%1$-20s %2$-30s ", " Skipped:", reporter.countSkips() ) );
		for( String skippedFile : reporter.getSkips() )
			logger.debug( "\t" + skippedFile );

		logger.info( "------------------------------------------------------------------------------------------------------------" );

		return 0;
	}

	/*
	 * Helper Methods
	 */

	private void processFile( File file ) {

		logger.info( "Analyzing " + objectType + " file: " + file.getName() );

		final String sourceId = getSourceReferenceFromFile( file );

		if ( simulate ) {

			logger.info( "\tFile [" + file.getName() + "]: Does not contain a valid source ID. Skipping..." );
			reporter.skipped( file.getAbsolutePath() );

		} else if ( StringUtils.length( sourceId ) == 32 ) {

			ResponseBody response = null;

			try {

				/*
				 * If the objectType is 'account' then we'll aggregate it as an account.  This is the default behavior.
				 */
				if ( DEFAULT_ACCOUNT_AGGREGATION.equalsIgnoreCase( objectType ) ) {

					logger.debug( "\tFile [" + file.getName() + "]: Submitting Account Aggregation: Source ID[" + sourceId + "], Disable Optimization[" + disableOptimization + "]" );
					response = sailPointService.aggregateAccounts(sourceId, disableOptimization, file);

				/*
				 * If the objectType is something else then we'll aggregate it as an entitlement.  Entitlement aggregations can be many different objectTypes.
				 */
				} else {

					logger.debug( "\tFile [" + file.getName() + "]: Submitting Entitlement Aggregation: Source ID[" + sourceId + "], Object Type[" + objectType + "]" );
					response = sailPointService.aggregateEntitlements(sourceId, objectType, file);

				}

				logger.debug( "\tFile [" + file.getName() + "]: Aggregation response: " + response.string() );

				logger.info( "\tFile [" + file.getName() + "]: Aggregated successfully." );
				reporter.success( file.getAbsolutePath() );

			} catch ( Exception e ) {

				logger.info( "\tFile [" + file.getName() + "]: Error: " + e.getMessage() );
				reporter.error( file.getAbsolutePath() );

			}

		} else {

			logger.info( "\tFile [" + file.getName() + "]: Does not contain a valid source ID. Skipping..." );
			reporter.skipped( file.getAbsolutePath() );
		}

	}

	private Map<String, String> buildSourceReferenceMap() {

		Map<String, String> sourceReferenceMap = new HashMap<String, String>();

		logger.debug( "------------------------------------------------------------------------------------------------------------" );
		logger.debug( " Building source file lookup table. Starting source iteration. " );
		logger.debug( " To avoid this scan, please switch to new Source IDs in your file names." );
		logger.debug( " Older Source ID references will not be used in the future." );
		logger.debug( "------------------------------------------------------------------------------------------------------------" );

		Iterator<Source> it = sailPointService.listSources();

		/*
		 * Iterate through all the sources in the system.  For customers with a lot of sources, this lookup could be somewhat painful.
		 * To avoid these kinds of lookups, we should move away from old CC IDs, and move to modern Source IDs. :)
		 */
		while (it.hasNext()) {

			Source source = it.next();
			String sourceName = source.getName();
			String oldSourceReference = (String) source.getConnectorAttribute("cloudExternalId");
			String newSourceReference = source.getId();

			/*
			 * This sourceReferenceMap is used to map old source IDs to new source IDs.
			 * Keyed by old source IDs; Values are new source IDs
			 * 184744 : 2c918087701c40cf01701dfdf2c61e2a
			 */
			if (oldSourceReference != null && newSourceReference != null && !oldSourceReference.equals("ccDecommissionId")) {
				logger.debug( String.format("%1$-10s %2$-40s ", " " + oldSourceReference, " : " + newSourceReference ) );
				sourceReferenceMap.put(oldSourceReference, newSourceReference);
			}

			//Add source name to id mapping as well for lookup by name
			if (sourceName != null && newSourceReference != null) {
				logger.debug( String.format("%1$-10s %2$-40s ", " " + sourceName, " : " + newSourceReference ) );
				sourceReferenceMap.put(sourceName, newSourceReference);
			}
		}

		logger.debug("------------------------------------------------------------------------------------------------------------");

		return sourceReferenceMap;

	}

	private String getSourceReferenceFromFile( File file ) {

		/*
		 * First, look for the new-form Source ID in the file name
		 * This can be found on the Source object as "id": "2c918087701c40cf01701dfdf2c61e2a"
		 * and consists of 32 characters of any 0-9, a-f
		 *
		 * We'll parse the file name, and extract the new-form Source ID
		 *   e.g., 2c918087701c40cf01701dfdf2c61e2a - Something.csv
		 * Would return "2c918087701c40cf01701dfdf2c61e2a"
 		 */
		Matcher newMatcher = Pattern
				.compile("^(\\s)?([0-9a-f]{32})")
				.matcher(file.getName());

		if (newMatcher.find()) {

			String fileSourceId = StringUtils.trim(newMatcher.group());

			logger.debug("\tFile [" + file.getName() + "]: detected with source ID reference [" + fileSourceId + "].");

			return fileSourceId;

		}

		/*
		 * Second, look for the old-form Source ID in the file name
		 * This is mainly there for backwards compatibility purposes.
		 *
		 * This can be found on the Source object under "connectorAttributes"
		 * as "cloudExternalId": "184744"
		 * and consists of up to 10 characters of any number (0-9)
		 *
		 * We'll parse the file name, and extract the old-form Source ID
		 *   e.g., 184744 - Something.csv
		 * Would return "184744"
		 */
		Matcher oldMatcher = Pattern
				.compile("^(\\s)?([0-9]{4,10})")
				.matcher(file.getName());

		if (oldMatcher.find()) {

			String fileSourceId = StringUtils.trim(oldMatcher.group());

			logger.debug("\tFile [" + file.getName() + "]: Detected with older source ID reference [" + fileSourceId + "]. Attempting to resolve.");

			/*
			 * First, check to see if we have a sourceReferenceMap defined.
			 * If it is null, it hasn't yet been initialized, so we enter here.
			 */
			if (this.sourceReferenceMap == null) {
				this.sourceReferenceMap = buildSourceReferenceMap();
			}

			/*
			 * At this point, the sourceReferenceMap is not null, and has already been initialized.
			 * Let's query what we have to see if we can resolve the oldSourceReference to the newSourceReference.
			 */

			if (this.sourceReferenceMap.containsKey(fileSourceId)) {

				logger.debug("\tFile [" + file.getName() + "]: Successfully resolved old source ID reference [" + fileSourceId + "] to new source ID reference [" + sourceReferenceMap.get(fileSourceId) + "]");
				return sourceReferenceMap.get(fileSourceId);

			} else {

				logger.error("\tFile [" + file.getName() + "]: Unable to resolve old source ID reference [" + fileSourceId + "] to new source ID reference. This file will be skipped.");
				return null;
			}
		}

		/*
		 * Third, we will assume there are no source IDs in the file, and attempt to do
		 * a lookup via name instead to fetch the appropriate source ID. This requires
		 * more calls to ISC, but is more convenient for customers who don't want to
		 * look up the ID in the UI, etc.
		 */
		String fileNameWithOutExt = file.getName().replaceFirst("[.][^.]+$", "");
		if (this.sourceReferenceMap == null) {
			this.sourceReferenceMap = buildSourceReferenceMap();
		}
		if (this.sourceReferenceMap.containsKey(fileNameWithOutExt)) {

			logger.debug("\tFile [" + file.getName() + "]: Successfully resolved source name reference [" + fileNameWithOutExt + "] to new source ID reference [" + sourceReferenceMap.get(fileNameWithOutExt) + "]");
			return sourceReferenceMap.get(fileNameWithOutExt);

		} else {

			logger.error("\tFile [" + file.getName() + "]: Unable to resolve source name reference [" + fileNameWithOutExt + "] to new source ID reference. This file will be skipped.");
			return null;
		}
	}
}
