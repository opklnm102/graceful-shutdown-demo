package me.opklnm102.springboot2_3_x;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Shutdown시 TaskExecutor의 task processing을 대기
 */
@Slf4j
public class TaskExecutorGracefulShutdownLifecycle implements SmartLifecycle {

    private final Duration timeout;

    private volatile boolean running;

    public TaskExecutorGracefulShutdownLifecycle(Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public void start() {
        this.running = true;
    }

    @Override
    public void stop() {
        this.running = false;
        doStop();
    }

    // DefaultLifecycleProcessor의 start, stop 동작 조건
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * SmartLifecycle Phase(default. Integer.MAX_VALUE)
     * when start - 0 -> 1 -> ... -> Integer.MAX_VALUE
     * when stop - Integer.MAX_VALUE -> ... -> 1 -> 0
     *
     * @return
     */
    @Override
    public int getPhase() {
        return SmartLifecycle.DEFAULT_PHASE - 1;
    }

    private void doStop() {
        log.info("Commencing graceful shutdown. Waiting for active task to complete");

        try {
            TimeUnit.SECONDS.sleep(timeout.toSeconds());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("Graceful shutdown complete");
    }
}
