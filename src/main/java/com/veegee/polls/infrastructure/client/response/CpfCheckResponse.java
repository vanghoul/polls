package com.veegee.polls.infrastructure.client.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CpfCheckResponse {
    private String status;
}
