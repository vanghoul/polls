package com.veegee.polls.business;

import com.veegee.polls.api.request.CreatePollRequest;
import com.veegee.polls.api.request.UpdatePollRequest;
import com.veegee.polls.api.request.VoteRequest;
import com.veegee.polls.business.exception.NotFoundException;
import com.veegee.polls.business.model.Voter;
import com.veegee.polls.business.model.factory.VoterFactory;
import com.veegee.polls.business.model.factory.PollFactory;
import com.veegee.polls.business.validation.VoteValidator;
import com.veegee.polls.infrastructure.repository.PollRepository;
import com.veegee.polls.business.model.Poll;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository repository;
    private final PollFactory pollFactory;
    private final VoterFactory voterFactory;
    private final VoteValidator voteValidator;

    public List<Poll> findAll() {
        return repository.findAll();
    }

    public Poll create(CreatePollRequest request) {
        Poll newPoll = pollFactory.newPoll(request);
        return repository.insert(newPoll);
    }

    public Poll update(String id, UpdatePollRequest request) {
        return repository.findById(id)
                .map(existingPoll -> {
                    Poll openPoll = pollFactory.openPoll(existingPoll, request);
                    repository.save(openPoll);
                    return openPoll;
                }).orElseThrow(() -> new NotFoundException(id));
    }

    public Poll vote(String id, VoteRequest request) {
        return repository.findById(id)
                .map(existingPoll -> {
                    Voter voter = voterFactory.newVoter(request);
                    if (voteValidator.isVoteValid(existingPoll, voter)) {
                        existingPoll.addVoter(voter);
                        repository.save(existingPoll);
                    }
                    return existingPoll;
                }).orElseThrow(() -> new NotFoundException(id));
    }
}
