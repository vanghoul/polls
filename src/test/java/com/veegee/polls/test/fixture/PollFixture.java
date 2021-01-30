package com.veegee.polls.test.fixture;

import com.veegee.polls.business.model.Poll;

import java.time.LocalDateTime;

import static com.veegee.polls.business.model.enumeration.StatusType.NEW;
import static com.veegee.polls.business.model.enumeration.StatusType.OPEN;
import static java.util.Collections.emptyList;

public class PollFixture {

    private static final String DEFAULT_ID = "ID";
    private static final String DEFAULT_TITLE = "TITLE";

    public static Poll defaultNewPoll() {
        return Poll.builder()
                    .id(DEFAULT_ID)
                    .title(DEFAULT_TITLE)
                    .status(NEW)
                    .voters(emptyList())
                .build();
    }

    public static Poll defaultRecentlyOpenPoll() {
        return Poll.builder()
                    .id(DEFAULT_ID)
                    .title(DEFAULT_TITLE)
                    .status(OPEN)
                    .start(LocalDateTime.now())
                    .end(LocalDateTime.now().plusHours(1L))
                    .voters(emptyList())
                .build();
    }
}
