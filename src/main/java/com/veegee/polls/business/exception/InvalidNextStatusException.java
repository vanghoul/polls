package com.veegee.polls.business.exception;

import com.veegee.polls.business.model.enumeration.StatusType;

import static java.lang.String.format;

public class InvalidNextStatusException extends RuntimeException {

    private static final String MESSAGE = "Invalid status change from %s to %s! (Should be %s)";
    private static final String DEFAULT_MESSAGE = "Invalid status change!";

    public InvalidNextStatusException(StatusType requested, StatusType current) {
        super(format(MESSAGE, requested, current, current.next()));
    }

    public InvalidNextStatusException() {
        super(DEFAULT_MESSAGE);
    }
}
