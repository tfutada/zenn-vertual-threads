package jp.tf.zennjdk21vthread;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;


@SpringBootApplication
public class Vt1 {
    private static final Logger log = LoggerFactory.getLogger(Vt1.class);
    //    private static final Lock lock = new ReentrantLock();
    private static final ThreadLocal<String> context = new ThreadLocal<>();

    @SneakyThrows
    private static void sleep(Duration duration) {
        Thread.sleep(duration);  // unmount(スレッドを譲る)
        log.info("!!! carrier thread is  {} ", Thread.currentThread());
    }

    @SneakyThrows
    private static void myTask(Duration duration) {
        log.info("『  {} : {} ", context.get(), Thread.currentThread());
        sleep(duration);  // unmount
//        fibonacciRecursive(47);
//        httpClient();
        log.info("』 {} ", Thread.currentThread());
    }

    private static long fibonacciRecursive(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }

    @SneakyThrows
    private static void httpClient() {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://pokeapi.co/api/v2/pokemon/ditto"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Status Code: {}", response.statusCode());
    }

    @SneakyThrows
    static void runMyTasks() {

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<Integer>> futures = new ArrayList<>();

//            var numCore = Runtime.getRuntime().availableProcessors();
            var numCore = 100_000;

            IntStream.range(0, numCore + 1).forEach(i -> {
                // Submit a task and get a Future
                Future<Integer> future = executor.submit(() -> {
                    context.set("task-" + i);
                    myTask(Duration.ofSeconds(5));
                    log.info("done task #{}", i);
                    return i;
                });
                futures.add(future);
            });

            log.info("submitted all tasks");

            // Wait for all tasks to complete
            for (var future : futures) {
                log.info("result: {}", String.valueOf(future.get()));
            }
        }
    }

    //
    public static void main(String[] args) {
        runMyTasks();
    }
}
