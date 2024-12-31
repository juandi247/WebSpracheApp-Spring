package com.sprache.juandiegodeutsch;


import com.sprache.juandiegodeutsch.config.CacheConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Import(CacheConfig.class)
public class RedisTest {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedisConnection() {
        try {
            redisTemplate.opsForValue().set("testKey", "Hola Redis!");

            String value = redisTemplate.opsForValue().get("testKey");

            assertThat(value).isEqualTo("Hola Redis!");
            System.out.println("Conexión a Redis exitosa. Valor obtenido: " + value);
        } catch (Exception e) {
            e.printStackTrace();
            assertThat(e).isNull();
        }
    }
}
