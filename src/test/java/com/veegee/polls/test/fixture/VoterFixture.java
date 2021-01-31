package com.veegee.polls.test.fixture;

import com.veegee.polls.business.model.Voter;

public class VoterFixture {

    private static final String DEFAULT_CPF = "123454321";

    public static Voter defaultVoter(boolean vote) {
        return Voter.builder()
                    .cpf(DEFAULT_CPF)
                    .vote(vote)
                .build();
    }
}
