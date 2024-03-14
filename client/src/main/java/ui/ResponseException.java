package ui;

public class ResponseException extends Exception {
    int statCode;

    public ResponseException(String message, int status) {
        super(message);
        this.statCode = status;
    }

    public int getStatCode() {
        return statCode;
    }

}
