package com.personal.cache.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 该配置不用啦，使用马士兵封装好的框架
 * @ClassName RedisConfig
 * @Author liupanpan
 * @Date 2026/1/9
 * @Description 将RedisTemplate的序列化方式改为JSON序列化，并特别处理了Java8日期时间类型，
 * 同时将key和hashKey都设置为字符串序列化，使得存储在Redis中的数据可读且跨语言友好。
 */
//@Configuration
public class RedisConfig {

    /**
     * 因为redis默认的序列化方式是byte[](JDK序列化),这种方式对redis图形化界面查看不太友好，序列化后的数据是二进制格式，可读性差，
     * 而且被序列化的对象必须实现java.io.Serializable接口，且序列化后的数据体积较大。
     * 所以要将key进行String序列化，value进行JSON序列化
     * 同时也是为了让RedisTemplate支持JDK8的日期格式
     */
//    @Bean
    public <T> RedisTemplate<String,T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        // 设置哈希（hash）数据结构的字段（field）的序列化方式。
        // 在Redis的Hash数据结构中，每个hash有一个key，而hash内部又有多个field-value对。
        // setHashKeySerializer就是设置这些field的序列化方式。
        redisTemplate.setHashKeySerializer(redisTemplate.getKeySerializer());

        redisTemplate.setValueSerializer(redisValueSerializer());
        redisTemplate.setHashValueSerializer(redisValueSerializer());
        // 保证生效
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }


    /*
     * 设置值的序列化方式：JSON序列化。
     * 创建了一个Jackson2JsonRedisSerializer，并配置了ObjectMapper，
     * 特别是对Java8日期时间类型（LocalDate、LocalDateTime）的序列化和反序列化支持。
     * 这样，当我们在Redis中存储对象时，对象会被序列化为JSON字符串，日期时间也会按照指定的格式进行序列化和反序列化，
     * 保证了数据的可读性和跨语言兼容性。
     */
//    @Bean
    public RedisSerializer<Object> redisValueSerializer() {
        // ObjectMapper 是 Jackson 库中最常用的类，用于在 Java 对象（POJO）和 JSON 数据之间进行转换（序列化和反序列化）。
        // 它可以将一个 Java 对象序列化成 JSON 字符串，也可以将一个 JSON 字符串反序列化成 Java 对象。
        // 使用 ObjectMapper 来定制 JSON 序列化的行为
        ObjectMapper objectMapper = new ObjectMapper();
        /*
         * 解决Java8日期时间类型的序列化问题 - 默认Jackson无法正确处理LocalDate/LocalDateTime
         */
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Module timeModule = new JavaTimeModule()
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter))
                .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateFormatter))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        // 注册时间模块
        objectMapper.registerModule(timeModule);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 设置对jdk8日期的支持
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return jackson2JsonRedisSerializer;
    }

}
