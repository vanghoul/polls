package com.veegee.polls.business;

import com.veegee.polls.api.request.CreatePollRequest;
import com.veegee.polls.api.request.UpdatePollRequest;
import com.veegee.polls.business.exception.NotFoundException;
import com.veegee.polls.business.model.factory.PollFactory;
import com.veegee.polls.infrastructure.repository.PollRepository;
import com.veegee.polls.business.model.Poll;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository repository;
    private final PollFactory factory;

    public List<Poll> findAll() {
        return repository.findAll();
    }

    public Poll create(CreatePollRequest request) {
        Poll newPoll = factory.newPoll(request);
        return repository.insert(newPoll);
    }

    public Poll update(String id, UpdatePollRequest request) {
        return repository.findById(id)
                .map(existingPoll -> {
                    Poll openPoll = factory.openPoll(existingPoll, request);
                    repository.save(openPoll);
                    return openPoll;
                }).orElseThrow(() -> new NotFoundException(id));
    }
}
