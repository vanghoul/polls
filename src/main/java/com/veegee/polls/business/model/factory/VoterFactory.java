package com.veegee.polls.business.model.factory;

import com.veegee.polls.api.request.VoteRequest;
import com.veegee.polls.business.model.Voter;
import org.springframework.stereotype.Component;

@Component
public class VoterFactory {

    public Voter newVoter(VoteRequest request) {
        return Voter.builder()
                    .cpf(request.getCpf())
                    .vote(request.getVote())
                .build();
    }

}
