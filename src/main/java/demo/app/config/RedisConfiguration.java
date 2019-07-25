package demo.app.config;

import demo.app.alert.interfaces.dto.Alert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by itaesu on 19/07/2019.
 */
@Profile("local")
@Configuration
public class RedisConfiguration {

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.host}")
    private String host;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

//
//    @Bean
//    public ReactiveRedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(this.host, this.redisPort);
//    }

    @Bean
    public ReactiveRedisTemplate<String, Alert> reactiveRedisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory) {
        return new ReactiveRedisTemplate<>(redisConnectionFactory, getStringAlertRedisSerializationContext());
    }

    @Bean
    public ReactiveRedisMessageListenerContainer container(ReactiveRedisConnectionFactory factory) {
        ReactiveRedisMessageListenerContainer container = new ReactiveRedisMessageListenerContainer(factory);
        return container;
    }

    @Bean
    public ReactiveRedisOperations<String, Alert> redisOperations(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, Alert> context = getStringAlertRedisSerializationContext();
        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public RedisSerializationContext<String, Alert> getStringAlertRedisSerializationContext() {
        Jackson2JsonRedisSerializer<Alert> serializer = new Jackson2JsonRedisSerializer<>(Alert.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Alert> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        return builder.value(serializer).build();
    }

}
