package org.example.service.product.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig
{
    public static StringRedisSerializer stringSerializer= new StringRedisSerializer();
    public static GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                //过期时间600秒
                .entryTtl(Duration.ofSeconds(600))
                // 配置序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(genericJackson2JsonRedisSerializer));

        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();

        return cacheManager;
    }

//    /**
//     * redis序列化的工具配置类，下面这个请一定开启配置
//     * 127.0.0.1:6379> keys *
//     * 1) "ord:102"  序列化过
//     * 2) "\xac\xed\x00\x05t\x00\aord:102"   野生，没有序列化过
//     * this.redisTemplate.opsForValue(); //提供了操作string类型的所有方法
//     * this.redisTemplate.opsForList(); // 提供了操作list类型的所有方法
//     * this.redisTemplate.opsForSet(); //提供了操作set的所有方法
//     * this.redisTemplate.opsForHash(); //提供了操作hash表的所有方法
//     * this.redisTemplate.opsForZSet(); //提供了操作zset的所有方法
//     * @param lettuceConnectionFactory
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory)
//    {
//        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//
//        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
//        //设置key序列化方式string
//        redisTemplate.setKeySerializer(stringSerializer);
//        //设置value的序列化方式json，使用GenericJackson2JsonRedisSerializer替换默认序列化
//        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
//
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
//
//        redisTemplate.afterPropertiesSet();
//
//        return redisTemplate;
//    }
}
