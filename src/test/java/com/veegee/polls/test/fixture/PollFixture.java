package com.veegee.polls.test.fixture;

import com.veegee.polls.business.model.Poll;
import com.veegee.polls.business.model.Voter;

import java.time.LocalDateTime;

import static com.veegee.polls.business.model.enumeration.StatusType.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class PollFixture {

    private static final String DEFAULT_ID = "ID";
    private static final String DEFAULT_TITLE = "TITLE";

    public static Poll newPoll() {
        return Poll.builder()
                    .id(DEFAULT_ID)
                    .title(DEFAULT_TITLE)
                    .status(NEW)
                    .voters(emptyList())
                .build();
    }

    public static Poll newBeforeInsertPoll(String title) {
        return Poll.builder()
                    .title(title)
                    .status(NEW)
                    .voters(emptyList())
                .build();
    }

    public static Poll recentlyOpenPoll() {
        return Poll.builder()
                    .id(DEFAULT_ID)
                    .title(DEFAULT_TITLE)
                    .status(OPEN)
                    .start(LocalDateTime.now())
                    .end(LocalDateTime.now().plusHours(1L))
                    .voters(emptyList())
                .build();
    }

    public static Poll openPoll(Voter... voters) {
        return Poll.builder()
                    .id(DEFAULT_ID)
                    .title(DEFAULT_TITLE)
                    .status(OPEN)
                    .start(LocalDateTime.now())
                    .end(LocalDateTime.now().plusHours(1L))
                    .voters(asList(voters))
                .build();
    }

    public static Poll closingSoonPoll(String title, Voter... voters) {
        return Poll.builder()
                    .id(DEFAULT_ID)
                    .title(title)
                    .status(OPEN)
                    .start(LocalDateTime.now().minusHours(1L))
                    .end(LocalDateTime.now())
                    .voters(asList(voters))
                .build();
    }
}
