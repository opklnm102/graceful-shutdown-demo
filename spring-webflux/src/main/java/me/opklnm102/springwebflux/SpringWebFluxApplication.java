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
                    log.info("pause");

                    for (int i = 0; i < 2; i++) {
                        CompletableFuture.runAsync(() -> {
                            // asynchronous
                            try {
                                log.info("doing task...");
                                TimeUnit.SECONDS.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            log.info("done task...");
                        });
                    }

                    // 10s 처리되는 작업
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                                         .body(BodyInserters.fromValue("ok"));
                });
    }
}
