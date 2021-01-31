package com.veegee.polls.infrastructure;

import com.veegee.polls.business.PollService;
import com.veegee.polls.business.model.Poll;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.javacrumbs.shedlock.core.LockAssert.assertLocked;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final PollService pollService;

    private static final int ONE_MINUTE_MILLIS = 60000;

    @Scheduled(fixedDelay = ONE_MINUTE_MILLIS)
    @SchedulerLock(name = "closePolls", lockAtLeastFor = "30s", lockAtMostFor = "2m")
    public void scheduleClosePolls() {
        assertLocked();
        List<Poll> closedPolls = pollService.closePolls();
        System.out.println("Fechei " + closedPolls);
    }

}
