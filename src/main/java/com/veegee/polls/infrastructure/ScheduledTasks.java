package com.veegee.polls.infrastructure;

import com.veegee.polls.business.PollService;
import com.veegee.polls.business.model.Poll;
import com.veegee.polls.infrastructure.messaging.PollClosedProducer;
import com.veegee.polls.infrastructure.messaging.model.Event;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

import static net.javacrumbs.shedlock.core.LockAssert.assertLocked;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final PollService service;
    private final PollClosedProducer producer;

    private static final int ONE_MINUTE_MILLIS = 60000;


    @Scheduled(fixedDelay = ONE_MINUTE_MILLIS)
    @SchedulerLock(name = "closePolls", lockAtLeastFor = "30s", lockAtMostFor = "2m")
    public void scheduleClosePolls() {
        assertLocked();
        List<Poll> closedPolls = service.closePolls();
        closedPolls.forEach(producer::sendEvent);
        System.out.println("Enviei " + closedPolls.size() + " eventos");
    }

    @Bean
    public Consumer<Event> pollClosedInput() {
        return event -> {
            System.out.println("Recebi " + event);
        };
    }
}
