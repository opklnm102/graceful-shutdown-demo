package me.opklnm102.springwebflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@Slf4j
public class SpringWebFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebFluxApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> composedRoutes() {
        return route(GET("/pause")
                        .and(accept(MediaType.TEXT_PLAIN))
                , request -> {
                    try {
                        TimeUnit.SECONDS.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                                         .body(BodyInserters.fromValue("ok"));
                })
                .andRoute(GET("/task")
                                .and(accept(MediaType.TEXT_PLAIN))
                        , request -> {

                            CompletableFuture.runAsync(() -> {
                                var next = true;
                                var maxCount = 10;
                                var count = 0;

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

                                log.info("done task...");
                            });

                            return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                                                 .body(BodyInserters.fromValue("ok"));
                        });
    }
}
