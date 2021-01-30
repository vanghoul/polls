package com.veegee.polls.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdatePollRequest {
    private String state;
    private Long durationInMillis;

    public UpdatePollRequest(String state) {
        this.state = state;
    }
}
