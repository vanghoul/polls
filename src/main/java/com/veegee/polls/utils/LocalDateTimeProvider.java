package com.veegee.polls.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LocalDateTimeProvider {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
