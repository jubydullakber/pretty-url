package com.stylight.url.lookup.exception;

public class InvalidUrlException extends RuntimeException{
    private static final String MESSAGE = "Url is invalid";

    public InvalidUrlException() {
        super(MESSAGE);
    }
}
