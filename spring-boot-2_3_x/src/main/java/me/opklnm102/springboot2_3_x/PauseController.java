package me.opklnm102.springboot2_3_x;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class PauseController {

    private ExecutorService executor;

    public PauseController() {
        executor = Executors.newFixedThreadPool(10);
    }

    @GetMapping(path = "/pause")
    public String pause() {
        CompletableFuture.runAsync(new LongTask(), executor);

        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "ok";
    }

    @Slf4j
    static class LongTask implements Runnable {

        boolean next = true;

        int maxCount = 10;

        @Override
        public void run() {
            int count = 0;

            while (!Thread.currentThread().isInterrupted() && next) {
                try {
                    log.info("doing task... count: {}", count);
                    TimeUnit.SECONDS.sleep(3);

                    count++;
                    if (count > maxCount) {
                        next = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
