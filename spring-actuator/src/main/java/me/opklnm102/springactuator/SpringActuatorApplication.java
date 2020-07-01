package me.opklnm102.springactuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * POST /actuator/shutdown가 호출되도 ContextClosedEvent는 Lifecycle에 따라 발행되므로 잡아서 처리해주면 된다
 */
@SpringBootApplication
public class SpringActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringActuatorApplication.class, args);
    }

}
