package sailpoint.test;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import sailpoint.utils.FileUploadUtility;

public class AggregateTest extends TestCase {

	String clientId = System.getenv("SP_CLIENT_ID" );
	String clientSecret = System.getenv("SP_CLIENT_SECRET" );

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testAggregation1() throws Exception {
		
		String args = "--url https://neil-test.api.identitynow.com --client_id " + System.getenv("SP_CLIENT_ID" ) + " --client_secret " + System.getenv("SP_CLIENT_SECRET" ) + " --file /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/ --recursive --verbose";

		FileUploadUtility.main( args.split( " " ) );

		assertTrue ( true );
	}

	@Test
	public void testAggregation2() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -f /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/ -v";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}

	@Test
	public void testAggregation3() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -f /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/ -x csv";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}
	
	@Test
	public void testAggregation4() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -f /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/ -x csv -x txt -R";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}
	
	@Test
	public void testAggregation5() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -f /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/184744-AuthEmployees.csv";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}
	
	
	@Test
	public void testAggregation6() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -f /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/81260-entitlement.csv -t";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}
	
	
	@Test
	public void testAggregation7() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -f /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/184744-AuthEmployees.csv -e /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/resources/81260-entitlement.csv";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}

	@Test
	public void testAggregation8() throws Exception {

		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -f /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/82343-acounts.csv -disableOptimization -R -v";

		FileUploadUtility.main( args.split( " " ) );

		assertTrue ( true );
	}

	@Test
	public void testAggregation9() throws Exception {

		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -e /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/82343-entitlements.csv";

		FileUploadUtility.main( args.split( " " ) );

		assertTrue ( true );
	}

	@Test
	public void testAggregation10() throws Exception {

		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -e /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/82343-roles.csv -type role";

		FileUploadUtility.main( args.split( " " ) );

		assertTrue ( true );
	}

	@Test
	public void testAggregation11() throws Exception {

		String args = "-url https://neil-test.api.identitynow.com -client_id " + clientId + " -client_secret " + clientSecret + " -f /Users/neil.mcglennon/Workspace/sailpoint-file-upload-utility/src/test/resources/2c918087701c40cf01701dfdf2c61e2a-AuthEmployees.csv";

		FileUploadUtility.main( args.split( " " ) );

		assertTrue ( true );
	}
}
