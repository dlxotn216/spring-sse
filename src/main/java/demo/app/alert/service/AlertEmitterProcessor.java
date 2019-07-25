package demo.app.alert.service;

import demo.app.alert.interfaces.dto.Alert;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;

/**
 * Created by itaesu on 24/07/2019.
 */
@Slf4j @Getter
@Component
public class AlertEmitterProcessor {
    private EmitterProcessor<Alert> emitterProcessor;
    private FluxSink<Alert> sink;

    public AlertEmitterProcessor() {
        this.emitterProcessor = EmitterProcessor.create();
        this.sink = this.emitterProcessor.sink();
    }

    public void emit(Alert alert){
        this.sink.next(alert);
    }
}
