package com.github.examples.config.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by igor on 4/7/18.
 */
public class FutureCompleatableExample {

    public static void main(String... args) throws ExecutionException, InterruptedException {
        System.out.println(Thread.currentThread().getId());
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hi");
            System.out.println(Thread.currentThread().getId());
            return "Hi";
        });
        future.getNow("bad");
        Thread.sleep(2000);
        future.thenApplyAsync(result -> {
            System.out.println(Thread.currentThread().getId());
            System.out.println(result + " all"); //output Hi all
            return result;
        });


        System.out.println();
    }
}
