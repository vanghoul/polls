package com.veegee.polls;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.veegee.polls.business.model.Poll;
import com.veegee.polls.infrastructure.messaging.model.Event;
import com.veegee.polls.infrastructure.repository.PollRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.veegee.polls.business.model.enumeration.StatusType.CLOSED;
import static com.veegee.polls.infrastructure.messaging.enumeration.EventType.POLL_CLOSED;
import static com.veegee.polls.test.fixture.PollFixture.closingSoonPoll;
import static com.veegee.polls.test.fixture.VoterFixture.randomVoter;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("IT")
@ExtendWith(SpringExtension.class)
@Import(TestChannelBinderConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(PER_CLASS)
public class PollsMessagingIT {

    @Autowired private PollRepository pollRepository;
    @Autowired private OutputDestination output;

    private ObjectMapper objectMapper;

    private static final String[] COOL_POLLS = {
            "Can the net amount of entropy of the universe be massively decreased?",
            "Does the Barber shave himself?",
            "Can an omnipotent being create a rock too heavy for itself to lift?"
    };

    @BeforeAll
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach
    void cleanup() {
        pollRepository.deleteAll();
    }

    @Test
    public void must_publish_poll_results_when_poll_is_closing() throws Exception {
        //given
        Poll closingSuccessfulPoll = pollRepository.save(closingSoonPoll(COOL_POLLS[0],
                randomVoter(true), randomVoter(true), randomVoter(false)));
        long waitForJob = Duration.ofSeconds(2).toMillis();

        //when
        sleep(waitForJob);

        //then
        Poll justClosed = pollRepository.findAll().get(0);
        Event sentEvent = objectMapper.readValue(output.receive().getPayload(), new TypeReference<>() {});

        assertThat(justClosed.getStatus()).isEqualTo(CLOSED);
        assertThat(sentEvent.getType()).isEqualTo(POLL_CLOSED);
        assertThat(sentEvent.getPayload().getPollId()).isEqualTo(closingSuccessfulPoll.getId());
        assertThat(sentEvent.getPayload().isPassed()).isTrue();
        assertThat(sentEvent.getPayload().getTotalVotes()).isEqualTo(COOL_POLLS.length);
    }
}