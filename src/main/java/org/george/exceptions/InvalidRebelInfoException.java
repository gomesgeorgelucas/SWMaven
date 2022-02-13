package org.george.exceptions;

public class InvalidRebelInfoException extends Exception{
    public  final String title = "Invalid Rebel Info";

    public InvalidRebelInfoException(String message) {
        super(message);
    }

    public InvalidRebelInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
