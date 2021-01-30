package com.veegee.polls.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreatePollRequest {
    private String title;
}
