package com;

import com.atjx.util.RunnableThreadWebCount;
import com.atjx.util.Timers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class demoApplication {



    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(demoApplication.class, args);
        //计数线程
        RunnableThreadWebCount runnableThreadWebCount = new RunnableThreadWebCount();
        runnableThreadWebCount.run();
        //计时器线程
        Timers timers = new Timers();
        timers.run();

    }

//    @Configuration
//    public static class RedisConfig {
//        @Bean
//        public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//            RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//            redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//
//            // 使用Jackson2JsonRedisSerialize 替换默认序列化
//            @SuppressWarnings("rawtypes")
//            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//
//
//            jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//
//            // 设置value的序列化规则和 key的序列化规则
//            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//            redisTemplate.setKeySerializer(new StringRedisSerializer());
//            redisTemplate.afterPropertiesSet();
//            return redisTemplate;
//        }
//    }
}




