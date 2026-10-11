package org.taxi.gateway;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.beans.factory.annotation.Qualifier;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties="eureka.client.enabled=false")
class GatewayContextTest {
 @Autowired @Qualifier("taxiRoutes") RouteLocator routes;
 @Test void gatewayContextAndRoutesLoad(){assertEquals(4, routes.getRoutes().collectList().block().size());}
}
