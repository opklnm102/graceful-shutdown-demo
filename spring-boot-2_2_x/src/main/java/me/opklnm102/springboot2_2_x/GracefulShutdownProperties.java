package me.opklnm102.springboot2_2_x;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "graceful.shutdown")
@Getter
@Setter
@Slf4j
@ToString
public class GracefulShutdownProperties implements InitializingBean {

    private Duration timeout = Duration.ofSeconds(30);

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info(toString());
    }
}
