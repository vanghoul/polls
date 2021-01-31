package com.veegee.polls.infrastructure;

import com.veegee.polls.business.PollService;
import com.veegee.polls.business.model.Poll;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final PollService pollService;

    private static final int ONE_MINUTE_MILLIS = 60000;

    @Scheduled(fixedDelay = ONE_MINUTE_MILLIS)
    public void scheduleClosePolls() {
        List<Poll> closedPolls = pollService.closePolls();
    }

}
