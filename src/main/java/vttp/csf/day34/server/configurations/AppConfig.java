package vttp.csf.day34.server.configurations;

import java.lang.System.Logger.Level;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {

    private Logger logger = Logger.getLogger(AppConfig.class.getName());

	@Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Optional<Integer> redisPort;

	@Bean
	@Scope("singleton")
	public RedisTemplate<String, Object> createRedis() {
		final RedisStandaloneConfiguration config 
		= new RedisStandaloneConfiguration();

	config.setHostName(redisHost);
	config.setPort(redisPort.get());

	final JedisClientConfiguration jedisClient = JedisClientConfiguration
			.builder()
			.build();
	final JedisConnectionFactory jedisFac = 
			new JedisConnectionFactory(config, jedisClient);
	jedisFac.afterPropertiesSet();
	
	RedisTemplate<String, Object> template 
			= new RedisTemplate<String, Object>();

	template.setConnectionFactory(jedisFac);
	template.setKeySerializer(new StringRedisSerializer());
	template.setHashKeySerializer(new StringRedisSerializer());

	RedisSerializer<Object> serializer
		= new JdkSerializationRedisSerializer(getClass().getClassLoader());
	template.setValueSerializer(serializer);

	return template;
	}
}
