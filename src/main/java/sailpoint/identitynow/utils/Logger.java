package sailpoint.identitynow.utils;

public class Logger {

	boolean verbose = false;
	
	public Logger(boolean verbose) {
		super();
		this.verbose = verbose;
	}

	public void error( String message ) {
		System.out.println( message );
	}
	
	public void info( String message ) {
		System.out.println( message );
	}
	
	public void debug( String message ) {
		if ( verbose )
			System.out.println( message );
	}
}