package com.veegee.polls.business.exception;

import static java.lang.String.format;

public class NotOpenException extends RuntimeException {

    private static final String MESSAGE = "Requested Poll with Id %s is not open!";

    public NotOpenException(String id) {
        super(format(MESSAGE, id));
    }
}
