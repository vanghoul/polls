package com.veegee.polls.infrastructure.messaging;

import com.veegee.polls.business.model.PollResult;
import com.veegee.polls.infrastructure.messaging.model.Event;
import com.veegee.polls.infrastructure.messaging.model.factory.EventFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PollClosedProducer {

    private final EventFactory factory;
    private final StreamBridge producer;

    private static final String OUTPUT_POLL_CLOSED = "pollClosedOut-out-0";

    public void sendEvent(PollResult poll) {
        Event event = factory.newEvent(poll);
        log.debug("Publishing event: {}", event);
        producer.send(OUTPUT_POLL_CLOSED, event);
    }
}
