package demo.app.alert.interfaces.publisher;

import demo.app.alert.interfaces.dto.Alert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by itaesu on 24/07/2019.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class RedisAlertPublisher {
    private final ReactiveRedisMessageListenerContainer reactiveRedisMessageListenerContainer;
    private final ReactiveRedisTemplate<String, Alert> reactiveRedisTemplate;

    @PostMapping(value = "/redis/alerts")
    public Mono<Map<String, Long>> push(@RequestBody Alert alert) {
        return this.reactiveRedisTemplate.convertAndSend("alerts", alert)
                                         .map(aLong -> {
                                             Map<String, Long> returnMap = new HashMap<>();
                                             returnMap.put("subscriberCount", aLong);
                                             return returnMap;
                                         });
        //Subscribe를 하지 않아서 발행이 제대로 안됐었음
        //해당 컨트롤러에서 Mono 반환하게 하면 될 듯
    }

    @GetMapping(value = "/redis/alerts/{receiver}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Alert>> subscribeRedis(@PathVariable("receiver") String receiver) {
        return this.reactiveRedisTemplate.listenTo(new ChannelTopic("alerts"))
                                         .doOnNext(o -> {
                                             log.info("redis template {}", o);
                                         })
                                         .map(s -> {
                                             log.info("Redis subscribe {}", s);
                                             return ServerSentEvent.<Alert>builder().event("redis-alert-" + s.getMessage().getReceiver())
                                                                                    .data(s.getMessage())
                                                                                    .build();
                                         });


        //단발로 끝나고 다음부턴 구독 되지 않음.. mono의 값도 0
        //다시 구독해야 또 단발로 하나 받음
        //        return this.reactiveRedisMessageListenerContainer.receive(channelTopic)
        //                                                         .map(ReactiveSubscription.Message::getMessage)
        //                                                         .map(s -> {
        //                                                             log.info("Redis subscribe {}", s);
        //                                                             return ServerSentEvent.<Alert>builder().event("redis")
        //                                                                                                    .data(new Alert(receiver,
        //                                                                                                                    s))
        //                                                                                                    .build();
        //                                                         })
        //                                                         .doOnError(throwable -> {
        //                                                             throwable.printStackTrace();
        //                                                             log.error("error on redis ", throwable);
        //                                                         });
    }
}
