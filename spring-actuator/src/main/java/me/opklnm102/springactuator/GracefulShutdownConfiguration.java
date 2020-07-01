package me.opklnm102.springactuator;

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
    public GracefulShutdownHandler gracefulShutdownHandler() {
        return new GracefulShutdownHandler();
    }
}
