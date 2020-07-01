package me.opklnm102.springboot2_2_x;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ContextClosedEvent 수신시 tomcat에서 new request를 수신하지 않도록 한 후 대기
 */
@Slf4j
public class TomcatConnectorGracefulShutdownHandler implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

    private static final int TIMEOUT = 60;

    private volatile Connector connector;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("graceful shutdown...");
        connector.pause();

        Executor executor = connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
            threadPoolExecutor.shutdown();

            try {
                if(!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                    log.warn("Tomcat thread pool did not shutdown gracefully within {} seconds. Proceeding with forceful shutdown", TIMEOUT);

                    threadPoolExecutor.shutdownNow();

                    if(!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                        log.error("Tomcat thread pool did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
