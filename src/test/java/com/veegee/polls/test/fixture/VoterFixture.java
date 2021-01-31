package com.veegee.polls.test.fixture;

import com.veegee.polls.business.model.Voter;

import java.util.UUID;

public class VoterFixture {

    private static final String DEFAULT_CPF = "123454321";

    public static Voter defaultVoter(boolean vote) {
        return Voter.builder()
                    .cpf(DEFAULT_CPF)
                    .vote(vote)
                .build();
    }

    public static Voter randomVoter(boolean vote) {
        String totallyNotCpf = UUID.randomUUID().toString();
        return Voter.builder()
                    .cpf(totallyNotCpf)
                    .vote(vote)
                .build();
    }
}
