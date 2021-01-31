package com.veegee.polls.infrastructure.messaging;

import com.veegee.polls.business.model.Poll;
import com.veegee.polls.infrastructure.messaging.model.Event;
import com.veegee.polls.infrastructure.messaging.model.factory.EventFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PollClosedProducer {

    private final EventFactory factory;
    private final StreamBridge producer;

    private static final String OUTPUT_POLL_CLOSED = "poll_closed_output";

    public boolean sendEvent(Poll poll) {
        Event event = factory.newEvent(poll);
        return producer.send(OUTPUT_POLL_CLOSED, event);
    }
}
