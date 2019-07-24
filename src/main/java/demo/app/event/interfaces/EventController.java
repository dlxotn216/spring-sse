package demo.app.event.interfaces;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by itaesu on 24/07/2019.
 */
@Slf4j
@RestController
public class EventController {

    @GetMapping("/simple-event-streams")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1)).map(this::getServerSentEvent);
    }

    private ServerSentEvent<String> getServerSentEvent(Long seq) {
        return ServerSentEvent.<String>builder()
                .id(String.valueOf(seq))
                .event("periodic-event")
                .data("SSE - " + LocalDateTime.now()).build();
    }
}
