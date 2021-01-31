package com.veegee.polls.business.exception;

import static java.lang.String.format;

public class AlreadyVotedException extends RuntimeException {

    private static final String MESSAGE = "Voter with Cpf %s has already voted for Poll %s!";

    public AlreadyVotedException(String voterCpf, String pollId) {
        super(format(MESSAGE, voterCpf, pollId));
    }

}
