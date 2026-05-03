package com.sweetcorner.incubyte_backend.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration      // tells spring boot that this is the configuration file for the project 
@EnableCaching      // tells sprong boot to start the caching supports for this project 
public class RedisConfig {

    @Bean           // create the object in the IOC and managed it 
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){
        RedisCacheConfiguration config=RedisCacheConfiguration
        .defaultCacheConfig().entryTtl(Duration.ofHours(1))
        .serializeKeysWith(RedisSerializationContext.SerializationPair
            .fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair
            .fromSerializer(new GenericJackson2JsonRedisSerializer()));

            return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build();
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Object> template=new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    } 
}

/*

First request ==> React → Spring → MySQL → Redis Save → Response

second response ==> React → Spring → Redis → Response

SO Spring must convert the data from 
        -->==>--> Java Object → JSON/String → Redis

        AND while fecthing from redis 
        Redis → Java Object  

        It's calles serialization and Deserialization Method


1) RedisConnectionFactory ---> this is used to create the connection between the spring <----> redis

2) RedisCacheManager      ---> this handles automatic caching  and it uses @Cacheable   in backend by just using the RedisCacheManager
    --> in this at first request the MySQL (it stores the data in redis) then for the next request the Redis is hit as it has stored the data in it  


3) TTL (.entryTtl)        ---> TTL(Time to Live) the stored cache in the redis database will bi directly expired in the next 1 hour

4) StringRedisSerializer (key Serializer)   ---> ---> this stores the redis key as the normal readable string in readable format 
    ---> without this the redis will store the data in the binary format ( non readable format)

5) GenericJackson2JsonRedisSerializer (Value Serializer) ---> ---> this converts the java objects into the JSON object 
    ---> [Java Object ] User(id=1,name="Abhishek")   to ---> ---> JSON {
                                                                            "id":"1",
                                                                            "name":"Abhishek"
                                                                            }

6) RedisTemplate<String,Object> (Redis Template) ---> ---> used for the manual redis operation 


7) RedisCacheConfiguration.defaultCacheConfig() [defaultCacheConfig()]  ---> starting template for Redis settings [Like buying a mobile with default settings
                                                                                                                    then changing wallpaper, ringtone, etc.
                                                                                                                    Same here.]

8) .serializeKeysWith(...)  ---> tells Redis : How to stroe the keys  , like ["user:101"] redis must save

9) RedisSerializationContext.SerializationPair ---> this islike the wrapper tells spring for -->[Use this serializer for Redis] because redis expects serializer in pair form.

10) .fromSerializer(new StringRedisSerializer()) --> convert the normal Java string into Redis storable format [used for: keys] .
        ---> why to use this ==> because redis keys should be :--> readable ,searchable and simple
        ---> without this you may sees this as :--> \xac\xed\x00\x05

11) .serializeValuesWith(...)  ---> tells redis to : "How to store the values " in [User object] OR [List<Product>]
        ---> it takes input as --> ' SerializationPair '

12) .builder(connectionFactory)  { RedisCacheManager.builder(connectionFactory) }  ---> create final "RedisCacheManager" using the Redis Connection 
        --> input: RedisConnectionFactory  --> " ## which connects to the redis server ## "
        --> output: return builder objects used to create the final cache manager 
        --> Real time example --> " building a house using water + cement + bricks "  where Builder prepare the final objects 
        
13) template.setConnectionFactory(connectionFactory); --> connects the Redis template  ↔  Redis Server
        without this template doesn't know where the Redis exits 
        if template is missing then --> RedisTemplate became useless and No redis operation is possible 

14) template.setKeySerializer() --> strore manual Redis key as String

15) template.setValueSerializer() --> stores manual redis value as JSON



*** suppose this goes in the redis then -->
redisTemplate.opsForValue()
.set("user:1", user);

then internally this happens
"user:1"
↓
StringRedisSerializer
↓
stored as String

User object
↓
Jackson Serializer
↓
stored as JSON


| Method                             | Purpose                           |
| ---------------------------------- | --------------------------------- |
| defaultCacheConfig()               | create base config                |
| serializeKeysWith()                | define key storage                |
| serializeValuesWith()              | define value storage              |
| StringRedisSerializer              | store keys as String              |
| GenericJackson2JsonRedisSerializer | store values as JSON              |
| builder()                          | create CacheManager               |
| setConnectionFactory()             | connect to Redis                  |
| setKeySerializer()                 | key serializer for manual Redis   |
| setValueSerializer()               | value serializer for manual Redis |
--------------------------------------------------------------------------
*/