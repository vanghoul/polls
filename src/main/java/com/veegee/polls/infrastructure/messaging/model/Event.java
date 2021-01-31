package com.veegee.polls.infrastructure.messaging.model;

import com.veegee.polls.business.model.Poll;
import com.veegee.polls.infrastructure.messaging.enumeration.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class Event {
    private final Poll payload;
    private final EventType type;
    private final LocalDateTime sentAt;
}
