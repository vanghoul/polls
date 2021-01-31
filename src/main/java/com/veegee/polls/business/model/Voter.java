package com.veegee.polls.business.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Voter {
    private final String cpf;
    private final boolean vote;
}
