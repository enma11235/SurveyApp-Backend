package com.quickpolls.exception;

public class OptionNotFoundException extends RuntimeException {
    public OptionNotFoundException(String message) {
        super(message);
    }
}
