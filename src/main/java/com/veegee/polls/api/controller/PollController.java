package com.veegee.polls.api.controller;

import com.veegee.polls.api.request.CreatePollRequest;
import com.veegee.polls.api.request.UpdatePollRequest;
import com.veegee.polls.business.PollService;
import com.veegee.polls.business.model.Poll;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api/v1/polls")
@RequiredArgsConstructor
public class PollController {

    private final PollService service;

    @GetMapping
    public ResponseEntity<List<Poll>> get() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<Poll> create(@RequestBody CreatePollRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody UpdatePollRequest request) {
        try {
            return ResponseEntity.ok(service.update(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
