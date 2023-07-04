package me.opklnm102.springboot2_3_x;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.context.LifecycleProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;
import java.util.concurrent.Executor;

@ConditionalOnProperty(value = "server.shutdown", havingValue = "graceful")
@Configuration
@Slf4j
public class GracefulShutdownConfiguration {

    /**
     * taskExecutorGracefulShutdownBeanPostProcessor 가 등록되면 이건 필요 없다
     *
     * @param lifecycleProperties
     * @return
     */
    @Bean
    public TaskExecutorGracefulShutdownLifecycle taskExecutorGracefulShutdownLifecycle(LifecycleProperties lifecycleProperties) {
        return new TaskExecutorGracefulShutdownLifecycle(lifecycleProperties.getTimeoutPerShutdownPhase());
    }

    /**
     * taskExecutorGracefulShutdownBeanPostProcessor 가 등록되면 이건 필요 없다
     *
     * @param executors
     * @param lifecycleProperties
     * @return
     */
    @Bean
    public WatchTaskExecutorGracefulShutdownLifecycle taskExecutorGracefulShutdownLifecycleWatchTasks(List<Executor> executors, LifecycleProperties lifecycleProperties) {
        return new WatchTaskExecutorGracefulShutdownLifecycle(executors, lifecycleProperties.getTimeoutPerShutdownPhase());
    }

    /**
     * 어딘가에서 선언한 ThreadPoolTaskExecutor를 graceful shutdown을 위해 customizing하기 위해 BeanPostProcessor 사용
     *
     * @param lifecycleProperties
     * @return
     */
    @Bean
    public BeanPostProcessor taskExecutorGracefulShutdownBeanPostProcessor(LifecycleProperties lifecycleProperties) {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof ThreadPoolTaskExecutor) {
                    ((ThreadPoolTaskExecutor) bean).setWaitForTasksToCompleteOnShutdown(true);
                    ((ThreadPoolTaskExecutor) bean).setAwaitTerminationSeconds(Math.toIntExact(lifecycleProperties.getTimeoutPerShutdownPhase().toSeconds()));
                } else if (bean instanceof ThreadPoolTaskScheduler) {
                    ((ThreadPoolTaskScheduler) bean).setWaitForTasksToCompleteOnShutdown(true);
                    ((ThreadPoolTaskScheduler) bean).setAwaitTerminationSeconds(Math.toIntExact(lifecycleProperties.getTimeoutPerShutdownPhase().toSeconds()));
                }

                return bean;
            }
        };
    }
}
