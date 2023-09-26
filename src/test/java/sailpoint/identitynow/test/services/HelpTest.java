package sailpoint.identitynow.test.services;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import sailpoint.identitynow.utils.FileUploadUtility;

public class HelpTest extends TestCase {
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testHelp() throws Exception {
		
		String args = "-help";
		
		FileUploadUtility.main( args.split( " " ) );
		
		assertTrue ( true );
	}

}
