package me.opklnm102.springactuator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.TimeUnit;

@Slf4j
public class GracefulShutdownHandler implements ApplicationListener<ContextClosedEvent> {

    private static final int TIMEOUT = 60;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("graceful shutdown...");

        try {
            TimeUnit.SECONDS.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
