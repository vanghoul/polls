package com.veegee.polls.business.validation;

import com.veegee.polls.infrastructure.client.CpfCheckerClient;
import com.veegee.polls.infrastructure.client.response.CpfCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class CpfValidator {

    private final CpfCheckerClient client;

    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

    public boolean isCpfValid(String cpf) {
        try {
            CpfCheckResponse response = client.checkCpf(cpf);
            return response.getStatus().equalsIgnoreCase(ABLE_TO_VOTE);
        } catch (HttpClientErrorException e) {
            return false;
        } catch (Exception e) {
            throw e;
        }
    }
}
