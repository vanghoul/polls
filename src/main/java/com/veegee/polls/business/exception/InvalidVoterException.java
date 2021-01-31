package com.veegee.polls.business.exception;

import static java.lang.String.format;

public class InvalidVoterException extends RuntimeException {

    private static final String MESSAGE = "Voter with Cpf %s is unable to vote or invalid!";

    public InvalidVoterException(String voterCpf) {
        super(format(MESSAGE, voterCpf));
    }

}
