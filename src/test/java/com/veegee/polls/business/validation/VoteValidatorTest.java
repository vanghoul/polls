package com.veegee.polls.business.validation;

import com.veegee.polls.business.exception.AlreadyVotedException;
import com.veegee.polls.business.exception.InvalidVoterException;
import com.veegee.polls.business.exception.NotOpenException;
import com.veegee.polls.business.model.Poll;
import com.veegee.polls.business.model.Voter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.veegee.polls.test.fixture.PollFixture.*;
import static com.veegee.polls.test.fixture.VoterFixture.defaultVoter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class VoteValidatorTest {

    @Mock private CpfValidator cpfValidator;

    private VoteValidator validator;

    @BeforeEach
    public void setup() {
        validator = new VoteValidator(cpfValidator);
    }

    @Test
    public void must_return_true_when_vote_is_valid() {
        //given
        Poll openPoll = recentlyOpenPoll();
        Voter voter = defaultVoter(true);

        given(cpfValidator.isCpfValid(voter.getCpf()))
                .willReturn(true);

        //when
        boolean isValid = validator.isVoteValid(openPoll, voter);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    public void must_throw_not_open_exception_when_poll_is_not_open() {
        //given
        Poll invalidStatusPoll = newPoll();
        Voter voter = defaultVoter(true);

        //when-then
        assertThatThrownBy(() ->validator.isVoteValid(invalidStatusPoll, voter))
                .isInstanceOf(NotOpenException.class);
    }

    @Test
    public void must_throw_invalid_voter_exception_when_voter_validation_returns_false() {
        //given
        Poll invalidStatusPoll = recentlyOpenPoll();
        Voter voter = defaultVoter(true);

        given(cpfValidator.isCpfValid(voter.getCpf()))
                .willReturn(false);

        //when-then
        assertThatThrownBy(() ->validator.isVoteValid(invalidStatusPoll, voter))
                .isInstanceOf(InvalidVoterException.class);
    }

    @Test
    public void must_throw_already_voted_exception_when_poll_is_not_open() {
        //given
        Voter voter = defaultVoter(true);
        Poll invalidStatusPoll = openPoll(voter);

        given(cpfValidator.isCpfValid(voter.getCpf()))
                .willReturn(true);

        //when-then
        assertThatThrownBy(() -> validator.isVoteValid(invalidStatusPoll, voter))
                .isInstanceOf(AlreadyVotedException.class);
    }
}
