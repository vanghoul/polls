package com.veegee.polls.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VoteRequest {
    @NotBlank(message = "Vote cpf cannot be null or empty!")
    private String cpf;

    @NotBlank(message = "Vote cannot be null or empty!")
    private Boolean vote;
}
