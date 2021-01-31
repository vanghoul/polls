package com.veegee.polls.business.exception;

import static java.lang.String.format;

public class NotClosedException extends RuntimeException {

    private static final String MESSAGE = "Requested Poll with Id %s is not closed yet!";

    public NotClosedException(String id) {
        super(format(MESSAGE, id));
    }
}
