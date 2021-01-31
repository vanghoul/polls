package com.veegee.polls.business.validation;

import com.veegee.polls.business.exception.AlreadyVotedException;
import com.veegee.polls.business.exception.InvalidVoterException;
import com.veegee.polls.business.exception.NotOpenException;
import com.veegee.polls.business.model.Poll;
import com.veegee.polls.business.model.Voter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.veegee.polls.business.model.enumeration.StatusType.OPEN;

@Component
@RequiredArgsConstructor
public class VoteValidator {

    private final CpfValidator cpfValidator;

    public boolean isVoteValid(Poll poll, Voter voter) {
        if (!isPollOpen(poll)) throw new NotOpenException(poll.getId());
        if (!isVoterValid(voter.getCpf())) throw new InvalidVoterException(voter.getCpf());
        if (hasAlreadyVoted(poll, voter)) throw new AlreadyVotedException(voter.getCpf(), poll.getId());

        return true;
    }

    private boolean isPollOpen(Poll poll) {
        return poll.getStatus() == OPEN;
    }

    private boolean isVoterValid(String cpf) {
        return cpfValidator.isCpfValid(cpf);
    }

    private boolean hasAlreadyVoted(Poll poll, Voter voter) {
        return poll.containsVoter(voter);
    }
}
