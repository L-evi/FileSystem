package com.levi;

import com.levi.client.TestClient;
import com.levi.utils.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@Slf4j
public class AnotherApplicationTests {

    @Resource
    private TestClient testClient;

    @Test
    public void testContext() {
        Result<String> result = testClient.testGet("hello world!");
        log.info("result {}", result.toString());
    }
}
