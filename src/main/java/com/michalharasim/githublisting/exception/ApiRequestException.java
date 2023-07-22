package com.michalharasim.githublisting.exception;

public class ApiRequestException extends RuntimeException {

    public ApiRequestException() {
        super("Unsuccesful github API call");
    }

    public ApiRequestException(String message) {
        super(message);
    }
}
