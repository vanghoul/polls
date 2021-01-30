package com.veegee.polls.infrastructure.repository;

import com.veegee.polls.business.model.Poll;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends MongoRepository<Poll, String> {
}
