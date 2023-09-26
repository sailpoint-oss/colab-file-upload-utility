package sailpoint.identitynow.test.services;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import sailpoint.identitynow.utils.FileUploadUtility;

public class AggregateTest extends TestCase {
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testAggregation1() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -f /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/ -R -v";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}

	@Test
	public void testAggregation2() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -f /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/ -v";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}

	@Test
	public void testAggregation3() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -f /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/ -x csv";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}
	
	@Test
	public void testAggregation4() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -f /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/ -x csv -x txt -R";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}
	
	@Test
	public void testAggregation5() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -f /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/resources/184744-AuthEmployees.csv";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}
	
	
	@Test
	public void testAggregation6() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -e /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/resources/81260-entitlement.csv";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}
	
	
	@Test
	public void testAggregation7() throws Exception {
		
		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -f /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/resources/184744-AuthEmployees.csv -e /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/resources/81260-entitlement.csv";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}

	@Test
	public void testAggregation8() throws Exception {

		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -f /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/resources/82343-acounts.csv -disableOptimization -R -v";

		FileUploadUtility.main( args.split( " " ) );

		assertTrue ( true );
	}

	@Test
	public void testAggregation9() throws Exception {

		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -e /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/resources/82343-entitlements.csv";

		FileUploadUtility.main( args.split( " " ) );

		assertTrue ( true );
	}

	@Test
	public void testAggregation10() throws Exception {

		String args = "-url https://neil-test.api.identitynow.com -client_id 550e2d8644e04beabaf860bf1cfd7b2a -client_secret 4fcd544ba6ea38c078f56331a75e662c02ca7470277f7929d1c1ffb4e11295e7 -e /Users/neil.mcglennon/Documents/Workspace/identitynow-services-tools/identitynow-file-upload-utility/src/test/resources/82343-roles.csv -type role";

		FileUploadUtility.main( args.split( " " ) );

		assertTrue ( true );
	}
}
