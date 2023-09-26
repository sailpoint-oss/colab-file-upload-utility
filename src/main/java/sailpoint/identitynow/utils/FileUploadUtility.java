package sailpoint.identitynow.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.Route;
import retrofit2.Response;
import sailpoint.identitynow.api.IdentityNowService;

public class FileUploadUtility {
	
	public static final String ABOUT_DATE = "2022-12-01 15:00 CST";
	public static final String ABOUT_VERSION = "3.0.5";
	public static final String ABOUT_LINK = "https://community.sailpoint.com/docs/DOC-3140";
	public static final String ACCOUNT_AGGREGATION = "Account";
	public static final String ENTITLEMENT_AGGREGATION = "Entitlement";
	
	@Parameter( names = { "-url" }, description = "IdentityNow API Gateway (e.g. https://tenant.api.identitynow.com)", order = 0 )
	private String url;
	
	@Parameter( names = { "-client_id" }, description = "IdentityNow Client ID (Personal Access Token)", order = 1 )
	private String clientId;
	
	@Parameter( names = { "-client_secret" }, description = "IdentityNow Client Secret (Personal Access Token)", order = 2 )
	private String clientSecret;
	
	@Parameter( names = { "-f", "-file" }, description = "Input Directory (e.g. /sailpoint/identitynow/files/)", converter = FileConverter.class, order = 3 )
	private List<File> files = null;
	
	@Parameter( names = { "-disableOptimization" }, description = "Disable Optimization for account aggregation", order = 4 )
	private boolean disableOptimization = false;
	
	@Parameter( names = { "-e", "-entitlement" }, description = "Input Directory (e.g. /sailpoint/identitynow/files/)", converter = FileConverter.class, order = 5 )
	private List<File> entitlements = null;
	
	@Parameter( names = { "-type" }, description = "Type - Use for specifying type of an Entitlement Aggregation", order = 6 )
	private String entitlementType = "group";
	
	@Parameter( names = { "-r", "-R" }, description = "Recursively search directories", order = 7 )
	private boolean recursive = false;
	
	@Parameter( names = { "-s", "-S", "-simulate" }, description = "Simulation mode", order = 8 )
	private boolean simulate = false;
	
	@Parameter( names = { "-t", "-timeout" }, description = "Timeout (in milliseconds)", order = 9 )
	private Long timeout = (long) 10000L;  // Default is 10s
	
	@Parameter( names = { "-x", "-extension" }, description = "File extensions to search (for directories only)", order = 10 )
	private List<String> fileExtensions = Arrays.asList( "csv" );
	
	@Parameter( names = { "-v", "-verbose" }, description = "Verbose logging, Default: false", order = 11 )
	private boolean verbose = false;
	
	@Parameter( names = { "-proxy_host" }, description = "Proxy Host", order = 12 )
	private String proxyHost;
	
	@Parameter( names = { "-proxy_port" }, description = "Proxy Post", order = 13 )
	private int proxyPort = -1;
		
	@Parameter( names = { "-proxy_type" }, description = "Proxy Type - Values can be: http, socks, direct", order = 14 )
	private String proxyType;
	
	@Parameter( names = { "-proxy_user" }, description = "Proxy User - Used for authenticated proxies", order = 15 )
	private String proxyUser = null;
	
	@Parameter( names = { "-proxy_password" }, description = "Proxy Password - Used for authenticated proxies", order = 16 )
	private String proxyPassword = null;
	
	@Parameter( names = { "-h", "-help", "-?" }, description = "Help", help = true, order = 17 )
	private boolean commandHelp;

	
	private Logger log = new Logger( verbose );
	
	public FileUploadUtility() {
		super();
	}
	
	public static void main( String[] args ) throws Exception {
		
		FileUploadUtility fileUploadUtility = new FileUploadUtility();
		fileUploadUtility.about();
		
        JCommander j = 
        	JCommander.newBuilder()
        	.addObject( fileUploadUtility )
        	.build();
        j.parse( args );
        j.setProgramName( "java -jar FileUploadUtility.jar" );
        
        switch( fileUploadUtility.getCommand() ) {
        
        		case "aggregate":
        			fileUploadUtility.commandAggregate();
        			break;

        		case "help":
        		default:
        			j.usage();
        			break;
        }
	}

	private String getCommand() {

		if ( files != null || entitlements != null)
			return "aggregate";
		if ( commandHelp )
			return "help";
		
		return "";
	}

	/*
	 * Command: Aggregate
	 */
	private void commandAggregate() {
		
		String fileType = null;
		List<File> lstFiles = null;
		log = new Logger( verbose );
		
		if ( url == null ) {
			log.error( "Usage: You must specify a -url parameter." );
			System.exit( 0 );
		}
		
		if ( !StringUtils.startsWithIgnoreCase( url, "https://" ) ) {
			log.error( "Usage: Your -url parameter must begin with 'https://'" );
			System.exit( 0 );
		}
		
		if ( !StringUtils.endsWithIgnoreCase( url, ".api.identitynow.com" ) ) {
			log.error( "Usage: Your -url parameter must be a valid API URL.  Check to make sure it is in the form 'https://tenant.api.identitynow.com'" );
			System.exit( 0 );
		}
		
		if ( clientId == null || clientSecret == null ) {
			log.info( "Usage: You must specify an IdentityNow Personal Access Token Client ID or Secret. Review the documentation for further details." );
			System.exit( 0 );
		}
		
		if(CollectionUtils.isNotEmpty(files) && CollectionUtils.isNotEmpty(entitlements)) {
			log.error( "Usage: The use of -f (uploading Accounts) and -e (uploading entitlements) at the same time is not permitted, please use either -f or -e " );
			System.exit( 0 );
		}
		
		if(CollectionUtils.isNotEmpty(files)) {
			lstFiles = files;
			fileType = ACCOUNT_AGGREGATION;
		} else if (CollectionUtils.isNotEmpty(entitlements)) {
			lstFiles = entitlements;
			fileType = ENTITLEMENT_AGGREGATION;
		}
		
		FileAggregator aggregator = new FileAggregator( url, clientId, clientSecret, proxyHost, proxyPort, proxyType, timeout );
		aggregator.execute( lstFiles, recursive, simulate, fileExtensions, fileType );
	}
	
	private void about() {
		
		log.info( "------------------------------------------------------------------------------------------------------------" );
		log.info( " SailPoint IdentityNow File Upload Utility" );
		log.info( "------------------------------------------------------------------------------------------------------------" );
		log.info( " Version:\t" + ABOUT_VERSION );
		log.info( " Date:\t\t" + ABOUT_DATE );
		log.info( " Docs:\t\t" + ABOUT_LINK );
		log.info( "------------------------------------------------------------------------------------------------------------" );
	}
	
	public class FileConverter implements IStringConverter<File> {
		@Override
		public File convert( String value ) {
			return new File( value );
		}
	}
	
	private class FileAggregator {
		
		IdentityNowService idnService;
		
		List<String> filesSuccess = new ArrayList<String>();
		List<String> filesError = new ArrayList<String>();
		List<String> filesSkipped = new ArrayList<String>();
		
		public FileAggregator ( String url, String clientId, String clientSecret, String proxyHost, int proxyPort, String proxyType, Long timeout ) {
			super();
			
			Proxy proxy = null;
			Authenticator proxyAuthenticator = null;
			
			if ( proxyHost != null && proxyPort > -1 ) {
				
				log.debug( "Proxy enabled!  Initializing proxy with settings: proxyHost[" + proxyHost + "], proxyPort[" + proxyPort + "], proxyType[" + proxyType + "]." );
				
				switch ( proxyType ) {
				
				case "direct" :
					proxy = new Proxy( Proxy.Type.DIRECT, new InetSocketAddress( proxyHost, proxyPort ) );
					break;	
				case "socks" : 
					proxy = new Proxy( Proxy.Type.SOCKS, new InetSocketAddress( proxyHost, proxyPort ) );
					break;
				default:
				case "http" :
					proxy = new Proxy( Proxy.Type.HTTP, new InetSocketAddress( proxyHost, proxyPort ) );
					break;
				}
				
				log.debug( "Proxy initialized! proxy[" + proxy + "]" );
				
				if ( proxyUser != null && proxyPassword != null ) {
					
					log.debug( "Proxy credentials detected.  Building proxy authenticator." );
					
					proxyAuthenticator = new Authenticator() {
						@Override
						public Request authenticate( Route route, okhttp3.Response response ) throws IOException {
							return response.request().newBuilder()
							          .header( "Proxy-Authorization", Credentials.basic( proxyUser, proxyPassword ) )
							          .build();
						}
					};
					
					log.debug( "Building proxy authenticator built." );

				}

			}
			
			this.idnService = new IdentityNowService( url, clientId, clientSecret, proxy, proxyAuthenticator, timeout );
		}

		public void execute( List<File> lstFiles, boolean recursive, boolean simulate, List<String> fileExtensions, String fileType ) {
			
			
			Timer.start();
			
			log.info( "------------------------------------------------------------------------------------------------------------" );
			log.info( " URL:\t\t\t" + url );
			
			
			if(ACCOUNT_AGGREGATION.equals(fileType)) {
				log.info( " "+ fileType +" Files:\t\t" + StringUtils.join( lstFiles, ", \n" ));
				log.info( " Optimization:\t\t" + ("true".equalsIgnoreCase(String.valueOf(disableOptimization))? "Disabled":"Enabled"));
			}
			else {
				log.info( " "+ fileType +" Files:\t" + StringUtils.join( lstFiles, ", \n" ));
				log.info( " Entitlement Type:\t" + entitlementType );
			}
				
			log.info( " Recursive:\t\t" + recursive );
			log.info( " Extensions:\t\t" + StringUtils.join( fileExtensions, "," ) );
			log.info( " Simulation:\t\t" + simulate );
			log.info( " Verbose:\t\t" + verbose );
			log.info( " Timeout:\t\t" + timeout );
			log.info( "------------------------------------------------------------------------------------------------------------" );
			
			log.info( "Checking credentials..." );
			
			try {
			
				idnService.createSession();
			
			} catch ( Exception e ) {
				log.error( "Error Logging into IdentityNow.  Check your credentials and try again. [" + e.getMessage() + "]" );
				System.exit(0);
			}
				
			if (CollectionUtils.isNotEmpty(lstFiles)) {
				for (File file : lstFiles) {

					if (file.isDirectory()) {
						log.info("Analyzing directory: " + file);

						@SuppressWarnings("unchecked")
						Iterator<File> fileIterator = FileUtils.iterateFiles(file,
								fileExtensions.stream().toArray(String[]::new), recursive);

						while (fileIterator.hasNext()) {
							processFile(fileIterator.next(), fileType);
						}

					} else {
						log.info("Analyzing "+fileType+" file: " + file);
						processFile(file, fileType);
					}
				}
			}
			
			
			log.info( "Complete." );
			
			log.info( "------------------------------------------------------------------------------------------------------------" );
			log.info( " Elapsed time:\t\t" + ( Timer.secondsElapsed() ) + " seconds" );
			log.info( " Total files processed:\t" + ( filesError.size() + filesSkipped.size() + filesSuccess.size() ) );
			log.info( "------------------------------------------------------------------------------------------------------------" );
			
			log.info( " Success:\t" + filesSuccess.size() );
			printFiles( filesSuccess );
			
			log.info( "------------------------------------------------------------------------------------------------------------" );
			
			log.info( " Error:\t\t" + filesError.size() );
			printFiles( filesError );
			
			log.info( "------------------------------------------------------------------------------------------------------------" );
			
			log.info( " Skipped:\t" + filesSkipped.size() );
			printFiles( filesSkipped );
			
			log.info( "------------------------------------------------------------------------------------------------------------" );
			
		}
		
		private void printFiles( List<String> files ) {
			
			if ( verbose ) {
				for (String file : files ) {
					log.info( "\t" + file );
				}
			}
		}
		
		private void processFile( File file, String fileType ) {
			
			log.info( "Analyzing "+fileType+" file: " + file.getName() );
			Response<ResponseBody> response = null;
						
			try {
				
				final String sourceId = getSourceId ( file );
				
				if ( simulate ) {
					
					filesSkipped.add( file.getAbsolutePath() );
					
				} else if ( !simulate && sourceId != null ){
					
					if(ACCOUNT_AGGREGATION.equals(fileType)) {						
						response = idnService
								.getAggregationService()
								.aggregateAccounts(
									sourceId,
									RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(disableOptimization)),
									RequestBody.create( MediaType.parse( "text/csv" ), file ) )
								.execute();
						
					} else if(ENTITLEMENT_AGGREGATION.equals(fileType)) {
						
						response = idnService
								.getAggregationService()
								.aggregateEntitlements( sourceId, entitlementType, RequestBody.create( MediaType.parse( "text/csv" ), file ) )
								.execute();
					}						
					
					if ( response.isSuccessful() ) {
						
						log.info( "\t"+fileType+" File [" + file.getName() + "] was aggregated successfully." );
						filesSuccess.add( file.getAbsolutePath() );
						
					} else {
						
						log.info( "\t"+fileType+" File [" + file.getName() + "] had an error: Code[" + response.code() + "] Message["+ response.message()  + "]" );
						filesError.add( file.getAbsolutePath() );
					}
					
				} else {
					
					log.info( "\t"+fileType+" File [" + file.getName() + "] does not contain a source ID. Skipping..." );
					filesSkipped.add( file.getAbsolutePath() );
				}
			
			} catch ( Exception e ) {
				
	 			log.error( "\t"+fileType+" File [" + file.getName() + "] had an error: "+ e.getMessage()  + "" );
				filesError.add( file.getAbsolutePath() );
			}

		}
		
		private String getSourceId ( File file ) throws Exception {
			Pattern p = Pattern.compile( "^(\\s)?([0-9]{1,10})" );
			Matcher matcher = p.matcher( file.getName() );
			return ( matcher.find() ) ? StringUtils.trim( matcher.group() ) : null;
		}
	}
	
}
