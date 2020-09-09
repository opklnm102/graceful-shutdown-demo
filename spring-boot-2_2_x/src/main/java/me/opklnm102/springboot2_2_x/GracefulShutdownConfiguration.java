package me.opklnm102.springboot2_2_x;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

@Profile(value = "graceful")
@Configuration
@EnableConfigurationProperties(value = GracefulShutdownProperties.class)
public class GracefulShutdownConfiguration {

    @Bean
    public TomcatConnectorGracefulShutdownHandler tomcatConnectorGracefulShutdownHandler(GracefulShutdownProperties gracefulShutdownProperties) {
        return new TomcatConnectorGracefulShutdownHandler(gracefulShutdownProperties.getTimeout().toSeconds());
    }

    @Bean
    public TaskExecutorGracefulShutdownHandler taskExecutorGracefulShutdownHandler(List<Executor> executors, GracefulShutdownProperties gracefulShutdownProperties) {
        return new TaskExecutorGracefulShutdownHandler(executors, gracefulShutdownProperties.getTimeout().toSeconds());
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(GracefulShutdownProperties gracefulShutdownProperties) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(Math.toIntExact(gracefulShutdownProperties.getTimeout().toSeconds()));
        taskExecutor.initialize();
        return taskExecutor;
    }
}
