package main.exceptions;

public class NotAccessNotFoundFileExc extends MyExceptions {

    public NotAccessNotFoundFileExc() {
    }

    public NotAccessNotFoundFileExc(String exception) {
        super(exception);
    }

    public NotAccessNotFoundFileExc(String message, Throwable cause) {
        super(message, cause);
    }
}
