package com.veegee.polls.test.fixture;

import com.veegee.polls.infrastructure.client.response.CpfCheckResponse;

public class CpfCheckFixture {

    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";
    private static final String UNABLE_TO_VOTE = "UNABLE_TO_VOTE";

    public static CpfCheckResponse validCpfResponse() {
        return CpfCheckResponse.builder()
                    .status(ABLE_TO_VOTE)
                .build();
    }

    public static CpfCheckResponse invalidCpfResponse() {
        return CpfCheckResponse.builder()
                    .status(UNABLE_TO_VOTE)
                .build();
    }
}
