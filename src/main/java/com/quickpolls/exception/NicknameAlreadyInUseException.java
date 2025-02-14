package com.quickpolls.exception;

public class NicknameAlreadyInUseException extends RuntimeException {
    public NicknameAlreadyInUseException(String message) {
        super(message);
    }
}