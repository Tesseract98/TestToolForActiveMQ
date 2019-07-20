package main;

public class MyExceptions extends Exception {
    public MyExceptions(String message) {
        super(message);
    }

    public MyExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    protected MyExceptions() {
    }
}
