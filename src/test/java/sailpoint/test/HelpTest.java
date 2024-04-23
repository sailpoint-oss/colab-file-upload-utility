package sailpoint.test;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import sailpoint.utils.FileUploadUtility;

public class HelpTest extends TestCase {
	
	@Before
	public void setUp() {
	}
	
	@Test
	public void testHelp1() throws Exception {
		
		String args = "--help";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}

	@Test
	public void testHelp2() throws Exception {

		String args = "-H";

		FileUploadUtility.main( args.split( " " ) );

		assertTrue ( true );
	}

	@Test
	public void testHelp3() throws Exception {

		FileUploadUtility.main( null );

		assertTrue ( true );
	}

}
