package com.phresh.exceptions;

public class RuleException extends Exception {

    public RuleException(String message) {
        super(message);
    }

    public RuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
