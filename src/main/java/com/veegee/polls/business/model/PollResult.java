package com.veegee.polls.business.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class PollResult {
    private String pollId;
    private String title;
    private long totalVotes;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private boolean passed;
}
