package com.veegee.polls.infrastructure;

import com.veegee.polls.business.PollService;
import com.veegee.polls.business.model.PollResult;
import com.veegee.polls.infrastructure.messaging.PollClosedProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.javacrumbs.shedlock.core.LockAssert.assertLocked;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final PollService service;
    private final PollClosedProducer producer;

    @Scheduled(fixedDelayString = "${jobs.closePolls.delayMillis}")
    @SchedulerLock(name = "${jobs.closePolls.lock.id}",
            lockAtLeastFor = "${jobs.closePolls.lock.min}", lockAtMostFor = "${jobs.closePolls.lock.max}")
    public void scheduleClosePolls() {
        log.info("Triggering ClosePolls job");
        int totalEvents;
        try {
            assertLocked();
            List<PollResult> closedPolls = service.closePolls();
            closedPolls.forEach(producer::sendEvent);
            totalEvents = closedPolls.size();
        } catch (Exception e) {
            log.error("Exception when triggering ClosePolls job: ", e);
            throw e;
        }
        log.info("ClosePolls job triggered successfully and produced {} events", totalEvents);
    }
}
