package com.thinkenterprise.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/threads")
public class ThreadsController {

    private static final Logger log = LoggerFactory.getLogger(ThreadsController.class);

    private final RestClient restClient;

    public ThreadsController(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder.baseUrl("http://httpbin.org/").build();
    }

    @GetMapping("/block/{seconds}")
    public String delay(@PathVariable(name = "seconds") int seconds) {
        var start = System.currentTimeMillis();

        var result = restClient.get()
                .uri("/delay/" + seconds)
                .retrieve()
                .toBodilessEntity();

        var end = System.currentTimeMillis();

        log.info("{} on {}, took {} ms ", result.getStatusCode(), end - start, Thread.currentThread());

        return Thread.currentThread().toString();
    }

    @GetMapping("/sleep/{seconds}")
    public String sleep(@PathVariable(name = "seconds") int seconds) throws InterruptedException {
        log.info("Start {}", Thread.currentThread());

        Thread.sleep(seconds * 1000L);

        log.info("End {}", Thread.currentThread());

        return Thread.currentThread().toString();
    }

}
