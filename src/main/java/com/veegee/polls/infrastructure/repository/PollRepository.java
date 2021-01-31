package com.veegee.polls.infrastructure.repository;

import com.veegee.polls.business.model.Poll;
import com.veegee.polls.business.model.enumeration.StatusType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PollRepository extends MongoRepository<Poll, String> {
    List<Poll> findAllByStatusAndEndBefore(StatusType status, LocalDateTime end);
}
