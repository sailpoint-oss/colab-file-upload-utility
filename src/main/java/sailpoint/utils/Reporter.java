package sailpoint.utils;

import java.util.ArrayList;
import java.util.List;

public class Reporter {

    private List<String> errorMessages;
    private List<String> skippedMessages;
    private List<String> successMessages;

    public Reporter() {
        this.successMessages = new ArrayList<>();
        this.errorMessages = new ArrayList<>();
        this.skippedMessages = new ArrayList<>();
    }

    public void success( String messageText ) {
        successMessages.add( messageText );
    }

    public void error( String messageText ) {
        errorMessages.add( messageText );
    }

    public void skipped( String messageText ) {
        skippedMessages.add( messageText );
    }

    public List<String> getSuccess() {
        return this.successMessages;
    }

    public List<String> getErrors() {
        return this.errorMessages;
    }

    public List<String> getSkips() {
        return this.skippedMessages;
    }

    public int countSuccess() {
        return this.successMessages.size();
    }

    public int countErrors() {
        return this.errorMessages.size();
    }

    public int countSkips() {
        return this.skippedMessages.size();
    }

    public int countTotal( ) {
        return this.successMessages.size() + this.errorMessages.size() + this.skippedMessages.size();
    }

}
