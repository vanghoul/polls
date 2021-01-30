package com.veegee.polls.business.model.factory;

import com.veegee.polls.api.request.CreatePollRequest;
import com.veegee.polls.api.request.UpdatePollRequest;
import com.veegee.polls.business.exception.InvalidNextStatusException;
import com.veegee.polls.business.model.Poll;
import com.veegee.polls.utils.LocalDateTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.veegee.polls.business.model.enumeration.StatusType.NEW;
import static com.veegee.polls.business.model.enumeration.StatusType.OPEN;
import static com.veegee.polls.test.fixture.PollFixture.defaultNewPoll;
import static com.veegee.polls.test.fixture.PollFixture.defaultRecentlyOpenPoll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PollFactoryTest {

    @Mock private LocalDateTimeProvider dateTimeProvider;

    private PollFactory factory;

    @BeforeEach
    public void setup() {
        factory = new PollFactory(dateTimeProvider);
    }

    @Test
    public void must_create_poll_with_given_title_when_creating_new_poll() {
        //given
        String expectedTitle = "Do Androids dream of Electric Sheep?";
        CreatePollRequest request = new CreatePollRequest(expectedTitle);

        //when
        Poll newPoll = factory.newPoll(request);

        //then
        assertThat(newPoll.getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    public void must_create_poll_with_NEW_status_when_creating_new_poll() {
        //given
        String title = "Is the ship of Theseus still the same ship?";
        CreatePollRequest request = new CreatePollRequest(title);

        //when
        Poll newPoll = factory.newPoll(request);

        //then
        assertThat(newPoll.getStatus()).isEqualTo(NEW);
    }

    @Test
    public void must_change_poll_to_OPEN_status_when_opening_poll() {
        //given
        Poll existingPoll = defaultNewPoll();
        String newStatus = "OPEN";
        Long duration = Duration.ofHours(1L).toMillis();
        UpdatePollRequest request = new UpdatePollRequest(newStatus, duration);

        given(dateTimeProvider.now())
                .willReturn(LocalDateTime.now());

        //when
        Poll openPoll = factory.openPoll(existingPoll, request);

        //then
        assertThat(openPoll.getStatus()).isEqualTo(OPEN);
    }

    @Test
    public void must_calculate_poll_ending_when_opening_poll() {
        //given
        Poll existingPoll = defaultNewPoll();
        String newStatus = "OPEN";
        Long duration = Duration.ofHours(1L).toMillis();
        UpdatePollRequest request = new UpdatePollRequest(newStatus, duration);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime expectedEnd = start.plusHours(1L);

        given(dateTimeProvider.now())
                .willReturn(start);

        //when
        Poll openPoll = factory.openPoll(existingPoll, request);

        //then
        assertThat(openPoll.getEnd()).isEqualTo(expectedEnd);
    }

    @Test
    public void must_calculate_poll_ending_in_one_minute_when_opening_poll_without_duration() {
        //given
        Poll existingPoll = defaultNewPoll();
        String newStatus = "OPEN";
        UpdatePollRequest request = new UpdatePollRequest(newStatus);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime expectedEnd = start.plusMinutes(1L);

        given(dateTimeProvider.now())
                .willReturn(start);

        //when
        Poll openPoll = factory.openPoll(existingPoll, request);

        //then
        assertThat(openPoll.getEnd()).isEqualTo(expectedEnd);
    }

    @Test
    public void must_throw_invalid_next_status_exception_when_opening_poll_not_in_NEW_status() {
        //given
        Poll existingPoll = defaultRecentlyOpenPoll();
        String newStatus = "OPEN";
        UpdatePollRequest request = new UpdatePollRequest(newStatus);

        //when-then
        assertThatThrownBy(() -> factory.openPoll(existingPoll, request))
                .isInstanceOf(InvalidNextStatusException.class);
    }
}
