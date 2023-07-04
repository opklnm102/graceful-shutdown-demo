package me.opklnm102.springboot2_3_x;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Shutdown시 TaskExecutor의 task processing을 대기
 */
@Slf4j
public class WatchTaskExecutorGracefulShutdownLifecycle implements SmartLifecycle {

    private final List<Executor> executors;

    private final Duration timeout;

    private volatile boolean running;

    public WatchTaskExecutorGracefulShutdownLifecycle(List<Executor> executors, Duration timeout) {
        this.executors = executors;
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

        for (int i = 0; i < timeout.toSeconds(); i++) {
            var activeTask = executors.stream()
                                      .map(executor -> {
                                          if (executor instanceof ThreadPoolTaskExecutor) {
                                              return ((ThreadPoolTaskExecutor) executor).getActiveCount();
                                          }
                                          if (executor instanceof ThreadPoolExecutor) {
                                              return ((ThreadPoolExecutor) executor).getActiveCount();
                                          }
                                          if (executor instanceof ThreadPoolTaskScheduler) {
                                              return ((ThreadPoolTaskScheduler) executor).getActiveCount();
                                          }
                                          return 0;
                                      })
                                      .reduce(0, Integer::sum);

            log.debug("active task : {}, time: {}", activeTask, i);
            if (activeTask <= 0) {
                break;
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        log.info("Graceful shutdown complete");
    }
}
