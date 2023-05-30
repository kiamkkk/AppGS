package com.gseek.gs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.pojo.data.ChatDO;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 设置redisTemplate.
 *
 * @author Phak
 * @since 2023/5/5-22:04
 */
@Component
public class RedisConfig {

    @Value("${spring.data.redis.database.db1}")
    private int db1;

    @Value("${spring.data.redis.database.db2}")
    private int db2;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.data.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.data.redis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.data.redis.pool.max-wait}")
    private int maxWait;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    @Lazy
    GenericObjectPoolConfig poolConfig;

    @Bean
    public GenericObjectPoolConfig getPoolConfig(){
        // 配置redis连接池
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        return poolConfig;
    }

    /**
     * 管理token.
     * */
    @Bean("template0")
    public StringRedisTemplate stringRedisTemplate(){
        return new StringRedisTemplate(
                redisConnectionFactory(db1)
        );
    }
    /**
     * 管理聊天消息.
     * */
    @Bean("template1")
    public RedisTemplate<String, ChatDO> chatDORedisTemplate(){
        RedisTemplate<String, ChatDO> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(redisConnectionFactory(db2));

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<ChatDO> jacksonSeial = new Jackson2JsonRedisSerializer(ChatDO.class);
        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 提供工厂.
     * */
    private RedisConnectionFactory redisConnectionFactory(int database) {
        // 构建工厂对象
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig);
        // 设置使用的redis数据库
        factory.setDatabase(database);
        // 重新初始化工厂
        factory.afterPropertiesSet();
        return factory;
    }
}
