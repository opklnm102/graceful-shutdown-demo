package me.opklnm102.springboot2_2_x;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ContextClosedEvent 수신시 tomcat에서 new request를 수신하지 않도록 한 후 대기
 */
@Slf4j
public class TaskExecutorGracefulShutdownHandler implements ApplicationListener<ContextClosedEvent> {

    private final List<Executor> executors;

    private final long timeout;

    public TaskExecutorGracefulShutdownHandler(List<Executor> executors, long timeout) {
        this.executors = executors;
        this.timeout = timeout;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("graceful shutdown...");

        try {
            TimeUnit.SECONDS.sleep(timeout);
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
