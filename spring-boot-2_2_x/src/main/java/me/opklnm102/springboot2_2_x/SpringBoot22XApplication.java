package me.opklnm102.springboot2_2_x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ContextClosedEvent 수신시 상황에 맞게 처리해줘야 한다
 *   web application이라면 new request를 수신하지 않도록
 *   scheduled task application이라면 새로운 task를 시작하지 않도록
 */
@SpringBootApplication
public class SpringBoot22XApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot22XApplication.class, args);
    }

}
