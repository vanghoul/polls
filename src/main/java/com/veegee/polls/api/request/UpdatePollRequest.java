package com.veegee.polls.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class UpdatePollRequest {
    @NotBlank(message = "Poll state cannot be null or empty!")
    private String state;
    private Long durationInMillis;

    public UpdatePollRequest(String state) {
        this.state = state;
    }
}
