package example.todolist.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableRedisRepositories("example.todolist.data.cache.repository")
public class RedisConfig {

    private static final int REDIS_POOL_MAX_IDLE = 10;
    private static final int REDIS_POOL_MIN_IDLE = 5;
    private static final int REDIS_POOL_MAX_CONNECTIONS = 30;
    private static final long REDIS_POOL_TIMEOUT = 30_000L;
    private static final long REDIS_POOL_IDLE_TIMEOUT = 15_000L;

    @Value("${redis.sentinel.host}")
    private String redisHost;

    @Value("${redis.sentinel.port}")
    private String redisPort;

    @Value("${redis.sentinel.enabled}")
    private String redisSentinelEnabled;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(REDIS_POOL_MAX_IDLE);
        poolConfig.setMinIdle(REDIS_POOL_MIN_IDLE);
        poolConfig.setMaxTotal(REDIS_POOL_MAX_CONNECTIONS);
        poolConfig.setMinEvictableIdleTimeMillis(REDIS_POOL_IDLE_TIMEOUT);
        poolConfig.setMaxWaitMillis(REDIS_POOL_TIMEOUT);
        poolConfig.setFairness(true);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        if (Boolean.TRUE.toString().equalsIgnoreCase(redisSentinelEnabled)) {
            Iterable<RedisNode> sentinelNodes = Arrays.asList(new RedisNode.RedisNodeBuilder().listeningAt(
                    redisHost, Integer.valueOf(redisPort)).build());
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
            redisSentinelConfiguration.setMaster("master");
            redisSentinelConfiguration.setSentinels(sentinelNodes);

            ClientOptions clusterClientOptions = ClientOptions.builder()
                    .socketOptions(SocketOptions.builder().connectTimeout(Duration.ofSeconds(30)).keepAlive(true).build())
                    .timeoutOptions(TimeoutOptions.builder().fixedTimeout(Duration.ofSeconds(30)).build())
                    .autoReconnect(true)
                    .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                    .build();
            LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                    .readFrom(ReadFrom.REPLICA_PREFERRED)
                    .clientOptions(clusterClientOptions)
                    .shutdownTimeout(Duration.ofSeconds(30))
                    .poolConfig(poolConfig)
                    .build();
            return new LettuceConnectionFactory(redisSentinelConfiguration, clientConfiguration);
        }

        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(redisHost);
        standaloneConfiguration.setPort(Integer.valueOf(redisPort));
        ClientOptions clusterClientOptions = ClientOptions.builder()
                .socketOptions(SocketOptions.builder().connectTimeout(Duration.ofSeconds(30)).keepAlive(true).build())
                .timeoutOptions(TimeoutOptions.builder().fixedTimeout(Duration.ofSeconds(30)).build())
                .autoReconnect(true)
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .build();
        LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .clientOptions(clusterClientOptions)
                .shutdownTimeout(Duration.ofSeconds(30))
                .poolConfig(poolConfig)
                .build();
        return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
    }

    @Bean
    public RedisTemplate redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setEnableTransactionSupport(false);
        redisTemplate.setStringSerializer(stringSerializer);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        redisTemplate.setDefaultSerializer(jsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
