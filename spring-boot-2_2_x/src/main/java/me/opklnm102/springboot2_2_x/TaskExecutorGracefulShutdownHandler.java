package me.opklnm102.springboot2_2_x;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskExecutorGracefulShutdownHandler implements ApplicationListener<ContextClosedEvent> {

    private static final int TIMEOUT = 60;

    private final List<Executor> executors;

    public TaskExecutorGracefulShutdownHandler(List<Executor> executors) {
        this.executors = executors;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("graceful shutdown...");

        try {
            TimeUnit.SECONDS.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (Executor executor : executors) {
            if (executor instanceof ThreadPoolTaskExecutor) {
                ((ThreadPoolTaskExecutor) executor).shutdown();
            }
            if (executor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.shutdown();
            }
        }
    }
}
