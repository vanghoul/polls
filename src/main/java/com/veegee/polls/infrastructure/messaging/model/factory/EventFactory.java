package com.veegee.polls.infrastructure.messaging.model.factory;

import com.veegee.polls.business.model.Poll;
import com.veegee.polls.infrastructure.messaging.model.Event;
import com.veegee.polls.utils.LocalDateTimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.veegee.polls.infrastructure.messaging.enumeration.EventType.POLL_CLOSED;

@Component
@RequiredArgsConstructor
public class EventFactory {

    private final LocalDateTimeProvider dateTimeProvider;

    public Event newEvent(Poll poll) {
        return Event.builder()
                    .payload(poll)
                    .type(POLL_CLOSED)
                    .sentAt(dateTimeProvider.now())
                .build();
    }
}
