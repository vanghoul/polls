package com.veegee.polls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.veegee.polls.api.request.CreatePollRequest;
import com.veegee.polls.api.request.UpdatePollRequest;
import com.veegee.polls.business.model.Poll;
import com.veegee.polls.infrastructure.repository.PollRepository;
import com.veegee.polls.test.fixture.PollFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.util.Arrays;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.veegee.polls.business.model.enumeration.StatusType.*;
import static com.veegee.polls.test.fixture.PollFixture.newBeforeInsertPoll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("IT")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(PER_CLASS)
public class PollsIT {

    @Autowired private WebApplicationContext context;
    @Autowired private PollRepository pollRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private static final String DEFAULT_ENCODING = "utf-8";
    private static final String ENDPOINT = "/api/v1/polls/";

    private static final String[] COOL_POLLS = {
            "Can the net amount of entropy of the universe be massively decreased?",
            "Does the Barber shave himself?",
            "Can an omnipotent being create a rock too heavy for itself to lift?"
    };

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
    }

    @BeforeEach
    void cleanup() {
        pollRepository.deleteAll();
    }

    @Test
    public void must_return_all_polls_in_database_when_getting_at_POLLS() throws Exception {
        //given
        Arrays.stream(COOL_POLLS)
                .map(PollFixture::newBeforeInsertPoll)
                .forEach(poll -> pollRepository.save(poll));

        //when-then
        mockMvc.perform(get(ENDPOINT)
                    .contentType(APPLICATION_JSON)
                    .characterEncoding(DEFAULT_ENCODING))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(COOL_POLLS.length)))
                .andExpect(jsonPath("$.[*].title", containsInAnyOrder(COOL_POLLS)));
    }

    @Test
    public void must_create_new_poll_in_database_when_posting_to_POLLS() throws Exception {
        //given
        String expectedTitle = COOL_POLLS[0];
        CreatePollRequest request = new CreatePollRequest(expectedTitle);

        //when
        mockMvc.perform(post(ENDPOINT)
                    .contentType(APPLICATION_JSON)
                    .characterEncoding(DEFAULT_ENCODING)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        //then
        Poll justCreated = pollRepository.findAll().get(0);
        assertThat(justCreated.getTitle()).isEqualTo(expectedTitle);
        assertThat(justCreated.getStatus()).isEqualTo(NEW);
    }

    @Test
    public void must_open_poll_in_database_when_patching_open_to_POLLS() throws Exception {
        //given
        Poll existingPoll = pollRepository.save(newBeforeInsertPoll(COOL_POLLS[0]));

        String open = OPEN.toString();
        Duration duration = Duration.ofDays(1);
        UpdatePollRequest request = new UpdatePollRequest(open, duration.toMillis());

        //when
        mockMvc.perform(patch(ENDPOINT + "/" + existingPoll.getId())
                .contentType(APPLICATION_JSON)
                .characterEncoding(DEFAULT_ENCODING)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        //then
        Poll justOpened = pollRepository.findAll().get(0);
        assertThat(justOpened.getStatus()).isEqualTo(OPEN);
        assertThat(justOpened.getEnd()).isEqualTo(justOpened.getStart().plus(duration));
    }
}
