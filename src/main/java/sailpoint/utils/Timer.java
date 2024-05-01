package sailpoint.utils;

public class Timer {

	private static long start;
	
	public static long start () {
		start = System.nanoTime();
		return start;
	}
	
	public static long elapsed() {
        return System.nanoTime() - start;
    }
	
	public static long secondsElapsed() {
		return (long) ( System.nanoTime() - start ) / 1000000000;
    }
	
    private Timer() {
        start();
    }
}
