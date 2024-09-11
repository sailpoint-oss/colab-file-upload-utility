package sailpoint.test;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import sailpoint.utils.SailPointUrl;

public class SailPointUrlTest extends TestCase {
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void test1() throws Exception {
		assertTrue ( SailPointUrl.isValid( "example.api.identitynow.com" ) );
	}

	@Test
	public void test2() throws Exception {
		assertTrue ( SailPointUrl.isValid( "example.api.identitynow-demo.com" ) );
	}

	@Test
	public void test3() throws Exception {
		assertTrue ( SailPointUrl.isValid( "example.api.saas.sailpointfedramp.com" ) );
	}

	@Test
	public void test4() throws Exception {
		assertFalse ( SailPointUrl.isValid( "example.identitynow.com" ) );
	}

	@Test
	public void test5() throws Exception {
		assertFalse ( SailPointUrl.isValid( "example.saas.sailpoint.com" ) );
	}
}
