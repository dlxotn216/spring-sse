package demo.app.temperature.interfaces;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;

/**
 * Created by itaesu on 24/07/2019.
 */
@Slf4j
@RestController
public class TemperatureController {

    @GetMapping(value = "/temperatures", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getTemperature() {
        Random random = new Random();
        final int low = 0;
        final int high = 50;

        return Flux.fromStream(
                IntStream.generate(() -> random.nextInt(high - low) + low)
                         .boxed()
                         .peek(integer -> log.info("Stream generate -> {} in thread {}", integer, currentThread().getName()))
                         .limit(10)
        ).delayElements(Duration.ofSeconds(1));
    }
}
