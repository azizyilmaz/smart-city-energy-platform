package com.smartcity.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
    }

}
