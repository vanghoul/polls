package com.veegee.polls.business.validation;

import com.veegee.polls.infrastructure.client.CpfCheckerClient;
import com.veegee.polls.infrastructure.client.response.CpfCheckResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.veegee.polls.test.fixture.CpfCheckFixture.invalidCpfResponse;
import static com.veegee.polls.test.fixture.CpfCheckFixture.validCpfResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CpfValidatorTest {

    @Mock private CpfCheckerClient client;

    private CpfValidator service;

    @BeforeEach
    public void setup() {
        service = new CpfValidator(client);
    }

    @Test
    public void must_return_true_when_cpf_check_is_able_to_vote() {
        //given
        String validCpf = "12345654321";
        CpfCheckResponse validResponse = validCpfResponse();

        given(client.checkCpf(validCpf))
                .willReturn(validResponse);

        //when
        boolean isValid = service.isCpfValid(validCpf);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    public void must_return_false_when_cpf_check_is_unable_to_vote() {
        //given
        String invalidCpf = "123456543210000000";
        CpfCheckResponse invalidResponse = invalidCpfResponse();

        given(client.checkCpf(invalidCpf))
                .willReturn(invalidResponse);

        //when
        boolean isValid = service.isCpfValid(invalidCpf);

        //then
        assertThat(isValid).isFalse();
    }

    @Test
    public void must_return_false_when_cpf_check_is_invalid() {
        //given
        String aCpf = "123456543210000000";

        given(client.checkCpf(aCpf))
                .willThrow(new RuntimeException("An Exception!!"));

        //when
        boolean isValid = service.isCpfValid(aCpf);

        //then
        assertThat(isValid).isFalse();
    }
}
