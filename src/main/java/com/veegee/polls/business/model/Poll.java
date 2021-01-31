package com.veegee.polls.business.model;

import com.veegee.polls.business.model.enumeration.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Document
@Getter
@ToString
public class Poll {

    @Id
    private final String id;
    private final String title;
    private final StatusType status;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final List<Voter> voters;
    private final Boolean result;

    public void addVoter(Voter voter) {
        voters.add(voter);
    }

    public boolean containsVoter(Voter voter) {
        return voters.stream().anyMatch(voters -> voters.getCpf().equalsIgnoreCase(voter.getCpf()));
    }
}
