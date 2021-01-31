package com.veegee.polls.api.controller;

import com.veegee.polls.api.request.CreatePollRequest;
import com.veegee.polls.api.request.UpdatePollRequest;
import com.veegee.polls.api.request.VoteRequest;
import com.veegee.polls.business.PollService;
import com.veegee.polls.business.model.Poll;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Api
@RestController
@RequestMapping("/api/v1/polls")
@RequiredArgsConstructor
@Slf4j
public class PollController {

    private final PollService service;

    @GetMapping
    public ResponseEntity<List<Poll>> get() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<Poll> create(@Valid @RequestBody CreatePollRequest request) {
        log.info("Received request to create Poll {}", request);
        return ResponseEntity.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @Valid @RequestBody UpdatePollRequest request) {
        try {
            log.info("Received request to make the following changes to Poll with Id {}: {}", id, request);
            return ResponseEntity.ok(service.update(id, request));
        } catch (Exception e) {
            log.error("Exception when making changes to Poll with Id {}:", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/vote/")
    public ResponseEntity<Object> vote(@PathVariable String id, @Valid @RequestBody VoteRequest request) {
        try {
            log.info("Received vote to Poll with Id {}: {}", id, request);
            return ResponseEntity.ok(service.vote(id, request));
        } catch (Exception e) {
            log.error("Exception when casting vote to Poll with Id {}:", id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
