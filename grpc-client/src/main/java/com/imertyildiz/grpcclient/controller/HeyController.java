package com.imertyildiz.grpcclient.controller;

import com.imertyildiz.grpcclient.Service.ClientService;
import com.imertyildiz.grpcproto.HelloWorldResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
public class HeyController {

    @Autowired
    private ClientService clientService;

    @GetMapping("send")
    public String saySmg(@RequestParam("client") String client, @RequestParam("msg") String msg) {
        clientService.sayHello(client, msg);
        return "sent";
    }

    @GetMapping("async")
    public Stream<String> saySmgAsync(@RequestParam("client") String client, @RequestParam("msg") String msg) {

        ExecutorService executorService = Executors.newFixedThreadPool(16);
        return IntStream.generate(()->new Random().nextInt(100))
                .limit(100)
                        .parallel()
                        .mapToObj(nr->{
                            return CompletableFuture.supplyAsync(()->clientService.sayAsyncHello(client+"_"+nr, msg+"_"+nr), executorService);
                        })
                .map(CompletableFuture::join)
                .map(HelloWorldResponse::getResponseMessage);
    }

    @GetMapping("non-async")
    public Stream<String> saySmgNonAsync(@RequestParam("client") String client, @RequestParam("msg") String msg) {

        return IntStream.generate(()->new Random().nextInt(100))
                .limit(100)
                .mapToObj(nr->{
                    HelloWorldResponse helloWorldResponse = clientService.sayHelloNonAsync(client+"_"+nr, msg+"_"+nr);
                    return helloWorldResponse.getResponseMessage();
                });
    }
}
