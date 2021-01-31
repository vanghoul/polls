package com.veegee.polls.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class VoteRequest {
    @NotBlank(message = "Vote cpf cannot be null or empty!")
    private String cpf;

    @NotNull(message = "Vote cannot be null!")
    private Boolean vote;
}
