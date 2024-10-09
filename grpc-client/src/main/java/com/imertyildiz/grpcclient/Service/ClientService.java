package com.imertyildiz.grpcclient.Service;

import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.imertyildiz.grpcproto.HelloWorldRequest;
import com.imertyildiz.grpcproto.HelloWorldResponse;
import com.imertyildiz.grpcproto.HelloWorldServiceGrpc.HelloWorldServiceBlockingStub;
import com.imertyildiz.grpcproto.HelloWorldServiceGrpc.HelloWorldServiceFutureStub;

import net.devh.boot.grpc.client.inject.GrpcClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @GrpcClient("grpc-server")
    private HelloWorldServiceBlockingStub helloWorldServiceBlockingStub;

    @GrpcClient("grpc-server")
    private HelloWorldServiceFutureStub helloWorldServiceFutureStub;

    public void sayHello(String sender, String message) {
        HelloWorldRequest helloWorldRequest =
                HelloWorldRequest
                        .newBuilder()
                        .setClientName(sender)
                        .setRequestMessage(message)
                        .build();
        HelloWorldResponse helloWorldResponse = this.helloWorldServiceBlockingStub.helloWorld(helloWorldRequest);
        logger.info(String.format("Server sent a response: %1s", helloWorldResponse.getResponseMessage()));
    }

    public HelloWorldResponse sayAsyncHello(String sender, String message) {
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        HelloWorldRequest helloWorldRequest =
                HelloWorldRequest
                        .newBuilder()
                        .setClientName(sender)
                        .setRequestMessage(message)
                        .build();

        ListenableFuture<HelloWorldResponse> futureResponse  = helloWorldServiceFutureStub.helloWorld(helloWorldRequest);
        CompletableFuture<HelloWorldResponse> completableFuture  = new CompletableFuture<>();
        futureResponse.addListener(()->{
            try{
                HelloWorldResponse helloWorldResponse = futureResponse.get();
                logger.info("We received {}", helloWorldResponse.getResponseMessage());
                completableFuture.complete(helloWorldResponse);
            } catch(InterruptedException|ExecutionException e) {
                logger.error("Error getting grpc response from server {}",e);
            }
        }, executorService);
        return completableFuture.join();//non blocking in parallel stream.
    }

    public HelloWorldResponse sayHelloNonAsync(String sender, String message) {
        HelloWorldRequest helloWorldRequest =
                HelloWorldRequest
                        .newBuilder()
                        .setClientName(sender)
                        .setRequestMessage(message)
                        .build();
        HelloWorldResponse helloWorldResponse = this.helloWorldServiceBlockingStub.helloWorld(helloWorldRequest);
        logger.info(String.format("Server sent a response: %1s", helloWorldResponse.getResponseMessage()));
        return helloWorldResponse;
    }

}