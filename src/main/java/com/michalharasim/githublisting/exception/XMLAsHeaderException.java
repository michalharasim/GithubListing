package com.michalharasim.githublisting.exception;

public class XMLAsHeaderException extends RuntimeException {

    public XMLAsHeaderException() {
        super("XML as Accept header is not acceptable! ");
    }

    public XMLAsHeaderException(String message) {
        super(message);
    }
}
