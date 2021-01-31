package com.veegee.polls.business.model.factory;

import com.veegee.polls.business.exception.NotClosedException;
import com.veegee.polls.business.model.Poll;
import com.veegee.polls.business.model.PollResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.veegee.polls.business.model.enumeration.StatusType.*;

@Component
@RequiredArgsConstructor
public class ResultFactory {

    public PollResult generate(Poll poll) {
        if (isPollClosed(poll)) {
            return PollResult.builder()
                        .pollId(poll.getId())
                        .title(poll.getTitle())
                        .totalVotes(poll.getVoters().size())
                        .startedAt(poll.getStart())
                        .endedAt(poll.getEnd())
                        .passed(poll.getResult())
                    .build();
        }

        throw new NotClosedException(poll.getId());
    }

    private boolean isPollClosed(Poll poll) {
        return poll.getStatus() == CLOSED;
    }
}
