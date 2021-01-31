package com.veegee.polls.infrastructure.client;

import com.veegee.polls.infrastructure.client.response.CpfCheckResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Component
public class CpfCheckerClient {

    private final RestTemplate restTemplate;

    private static final String USERS_ENDPOINT = "/users/%s";

    public CpfCheckerClient(@Value("${cpf-check-host}") String hostUrl) {
        this.restTemplate = new RestTemplateBuilder().rootUri(hostUrl).build();
    }

    public CpfCheckResponse checkCpf(String cpf) {
        return restTemplate.getForObject(format(USERS_ENDPOINT, cpf), CpfCheckResponse.class);
    }
}
