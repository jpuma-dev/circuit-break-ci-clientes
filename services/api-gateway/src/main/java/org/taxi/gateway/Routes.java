package org.taxi.gateway;
import org.springframework.context.annotation.*;
import org.springframework.cloud.gateway.route.*;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
@Configuration public class Routes {
 @Bean RouteLocator taxiRoutes(RouteLocatorBuilder b) { return b.routes()
 .route("clientes",r->r.path("/api/clientes/**").uri("lb://clientes-service"))
 .route("taxis",r->r.path("/api/taxis/**").uri("lb://taxis-service"))
 .route("viajes",r->r.path("/api/viajes/**").uri("lb://viajes-service"))
 .route("notificaciones",r->r.path("/api/notificaciones/**").uri("lb://notificaciones-service")).build(); }
}
