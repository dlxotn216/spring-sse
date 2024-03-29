package demo.app.alert.interfaces.publisher;

import demo.app.alert.interfaces.dto.Alert;
import demo.app.alert.service.AlertEmitterProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Created by itaesu on 24/07/2019.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class AlertPublisher {
    private final AlertEmitterProcessor alertEmitterProcessor;

    @GetMapping(value = "/alerts/{receiver}/publish")
    public void push(@PathVariable("receiver") String receiver, @RequestParam("message") String message) {
        this.alertEmitterProcessor.emit(new Alert(receiver, message));
    }

    @GetMapping(value = "/alerts/{receiver}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Alert>> subscribe(@PathVariable("receiver") String receiver) {
        return this.alertEmitterProcessor.getEmitterProcessor()
                                         .map(this::getAlertServerSentEvent)
                                         .log("request processed in current thread " + Thread.currentThread().getName())
                                         .doOnError(throwable -> {
                                             log.error("Error in publisher", throwable);
                                         });
    }


    private ServerSentEvent<Alert> getAlertServerSentEvent(Alert alert) {
        return ServerSentEvent.<Alert>builder().event("alert-" + alert.getReceiver()).data(alert)
                                               .retry(Duration.ofSeconds(10)).build();
    }
}
