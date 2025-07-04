package sailpoint.exception;

public class FileReadException extends RuntimeException{
    public FileReadException(String message) {
        super(message);
    }

    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileReadException(Throwable cause) {
        super(cause);
    }
}
