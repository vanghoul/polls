package com.veegee.polls.business.exception;

import static java.lang.String.format;

public class NotFoundException extends RuntimeException {

    private static final String MESSAGE = "Requested Poll with Id %s does not exist!";

    public NotFoundException(String id) {
        super(format(MESSAGE, id));
    }
}
