package com.veegee.polls.business.model.factory;

import com.veegee.polls.api.request.CreatePollRequest;
import com.veegee.polls.api.request.UpdatePollRequest;
import com.veegee.polls.business.exception.InvalidNextStatusException;
import com.veegee.polls.business.model.Poll;
import com.veegee.polls.business.model.Voter;
import com.veegee.polls.business.model.enumeration.StatusType;
import com.veegee.polls.utils.LocalDateTimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.veegee.polls.business.model.enumeration.StatusType.*;
import static java.util.Collections.emptyList;

@Component
@RequiredArgsConstructor
public class PollFactory {

    private static final Long ONE_MINUTE_MILLIS = Duration.ofMinutes(1L).toMillis();

    private final LocalDateTimeProvider dateTimeProvider;

    public Poll newPoll(CreatePollRequest creationRequest) {
        return Poll.builder()
                    .title(creationRequest.getTitle())
                    .status(NEW)
                    .voters(emptyList())
                .build();
    }

    public Poll openPoll(Poll existingPoll, UpdatePollRequest updateRequest) {
        StatusType requestedStatus = StatusType.valueOf(updateRequest.getState());

        if (isNextStatusValid(requestedStatus, existingPoll.getStatus())) {
            LocalDateTime start = dateTimeProvider.now();
            LocalDateTime end = start.plus(durationFromMillis(updateRequest.getDurationInMillis()));

            return Poll.builder()
                        .id(existingPoll.getId())
                        .title(existingPoll.getTitle())
                        .status(OPEN)
                        .start(start)
                        .end(end)
                        .voters(existingPoll.getVoters())
                    .build();
        }

        throw new InvalidNextStatusException(requestedStatus, existingPoll.getStatus());
    }

    public Poll closePoll(Poll pollToClose) {
        if (isNextStatusValid(CLOSED, pollToClose.getStatus())) {
            boolean result = calculateResult(pollToClose);
            return Poll.builder()
                        .id(pollToClose.getId())
                        .title(pollToClose.getTitle())
                        .status(CLOSED)
                        .start(pollToClose.getStart())
                        .end(pollToClose.getEnd())
                        .voters(pollToClose.getVoters())
                        .result(result)
                    .build();
        }

        throw new InvalidNextStatusException(CLOSED, pollToClose.getStatus());
    }

    private boolean isNextStatusValid(StatusType next, StatusType current) {
        return next == current.next();
    }

    private Duration durationFromMillis(Long duration) {
        return Duration.ofMillis(Optional.ofNullable(duration).orElse(ONE_MINUTE_MILLIS));
    }

    private boolean calculateResult(Poll closedPoll) {
        long yes = closedPoll.getVoters().stream().filter(Voter::isVote).count();
        long no = (closedPoll.getVoters().size() - yes);
        return yes > no;
    }
}
