package com.veegee.polls.business;

import com.veegee.polls.api.request.CreatePollRequest;
import com.veegee.polls.api.request.UpdatePollRequest;
import com.veegee.polls.api.request.VoteRequest;
import com.veegee.polls.business.exception.InvalidNextStatusException;
import com.veegee.polls.business.exception.NotFoundException;
import com.veegee.polls.business.exception.NotImplementedException;
import com.veegee.polls.business.model.PollResult;
import com.veegee.polls.business.model.Voter;
import com.veegee.polls.business.model.enumeration.StatusType;
import com.veegee.polls.business.model.factory.ResultFactory;
import com.veegee.polls.business.model.factory.VoterFactory;
import com.veegee.polls.business.model.factory.PollFactory;
import com.veegee.polls.business.validation.VoteValidator;
import com.veegee.polls.infrastructure.repository.PollRepository;
import com.veegee.polls.business.model.Poll;
import com.veegee.polls.utils.LocalDateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.veegee.polls.business.model.enumeration.StatusType.OPEN;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
public class PollService {

    private final PollRepository repository;
    private final PollFactory pollFactory;
    private final VoterFactory voterFactory;
    private final ResultFactory resultFactory;
    private final VoteValidator voteValidator;
    private final LocalDateTimeProvider dateTimeProvider;

    public List<Poll> findAll() {
        return repository.findAll();
    }

    public Poll create(CreatePollRequest request) {
        Poll newPoll = pollFactory.newPoll(request);
        log.debug("Saving in database: {}", newPoll);
        return repository.insert(newPoll);
    }

    public Poll update(String id, UpdatePollRequest request) {
        return repository.findById(id)
                .map(existingPoll -> {
                    switch (StatusType.valueOf(request.getState())) {
                        case NEW:
                            throw new InvalidNextStatusException();
                        case OPEN:
                            Poll openPoll = pollFactory.openPoll(existingPoll, request);
                            log.debug("Opening Poll in database: {}", openPoll);
                            repository.save(openPoll);
                            return openPoll;
                        default:
                            throw new NotImplementedException();
                    }
                }).orElseThrow(() -> new NotFoundException(id));
    }

    public Poll vote(String id, VoteRequest request) {
        return repository.findById(id)
                .map(existingPoll -> {
                    Voter voter = voterFactory.newVoter(request);
                    log.debug("Validating vote: {}", voter);
                    if (voteValidator.isVoteValid(existingPoll, voter)) {
                        log.debug("Vote {} is valid", voter);
                        existingPoll.addVoter(voter);
                        log.debug("Casting vote for Poll with Id {} to database: {}", existingPoll.getId(), voter);
                        repository.save(existingPoll);
                    }
                    return existingPoll;
                }).orElseThrow(() -> new NotFoundException(id));
    }

    public List<PollResult> closePolls() {
        List<Poll> closedPolls = getPollsReadyToBeClosed();
        return closedPolls.stream()
                .map(pollFactory::closePoll)
                .peek(poll -> log.debug("Closing Poll in database: {}", poll))
                .map(repository::save)
                .map(resultFactory::generate)
                .collect(toList());
    }

    private List<Poll> getPollsReadyToBeClosed() {
        return repository.findAllByStatusAndEndBefore(OPEN, dateTimeProvider.now());
    }
}
