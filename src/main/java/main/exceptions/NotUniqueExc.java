package main.exceptions;

public class NotUniqueExc extends MyExceptions {

    public NotUniqueExc() {
    }

    public NotUniqueExc(String msg) {
        super(msg);
    }

    public NotUniqueExc(String message, Throwable cause) {
        super(message, cause);
    }

}
