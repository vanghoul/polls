package com.veegee.polls.business.exception;

public class NotImplementedException extends RuntimeException {

    private static final String MESSAGE = "Requested operation is not available at this time!";

    public NotImplementedException() {
        super(MESSAGE);
    }
}
