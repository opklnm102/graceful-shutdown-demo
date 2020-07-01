package me.opklnm102.springboot2_2_x;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

@Configuration
public class GracefulShutdownConfiguration {

    @Profile(value = "graceful")
    @Bean
    public TomcatConnectorGracefulShutdownHandler tomcatConnectorGracefulShutdownHandler() {
        return new TomcatConnectorGracefulShutdownHandler();
    }

    @Profile(value = "graceful")
    @Bean
    public TaskExecutorGracefulShutdownHandler taskExecutorGracefulShutdownHandler(List<Executor> executors) {
        return new TaskExecutorGracefulShutdownHandler(executors);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return taskExecutor;
    }
}
