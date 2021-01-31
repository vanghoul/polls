package com.veegee.polls.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePollRequest {
    @NotBlank(message = "Poll title cannot be null or empty!")
    private String title;
}
