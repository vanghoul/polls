package com.veegee.polls.business.model.factory;

import com.veegee.polls.business.exception.NotClosedException;
import com.veegee.polls.business.model.Poll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.veegee.polls.test.fixture.PollFixture.recentlyOpenPoll;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResultFactoryTest {

    private ResultFactory factory;

    @BeforeEach
    public void setup() {
        factory = new ResultFactory();
    }

    @Test
    public void must_throw_exception_when_generating_results_for_not_closed_yet_poll() {
        //given
        Poll notClosedPoll = recentlyOpenPoll();

        //when-then
        assertThatThrownBy( () -> factory.generate(notClosedPoll))
                .isInstanceOf(NotClosedException.class);
    }
}
