package com.veegee.polls.infrastructure.messaging.model;

import com.veegee.polls.business.model.PollResult;
import com.veegee.polls.infrastructure.messaging.enumeration.EventType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class Event {
    private PollResult payload;
    private EventType type;
    private LocalDateTime sentAt;
}
