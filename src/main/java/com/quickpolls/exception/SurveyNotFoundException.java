package com.quickpolls.exception;

public class SurveyNotFoundException extends RuntimeException {
    public SurveyNotFoundException(String message) {
        super(message);
    }
}
