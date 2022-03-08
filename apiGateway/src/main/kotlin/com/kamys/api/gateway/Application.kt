package com.kamys.api.gateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod


@SpringBootApplication
@EnableDiscoveryClient
class Application

fun main() {
    SpringApplication.run(Application::class.java)

    println("ApiGateway run!")
}


@Configuration
internal class ProxyConfig {
    @Bean
    fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        val router = builder.routes()
            .route("projects") { route ->
                route.path("/projects/**")
                    .and()
                    .method(HttpMethod.GET)
                    .filters { it.stripPrefix(1) }
                    .uri("lb://projects")
            }
            .route("emails") { route ->
                route.path("/emails/**")
                    .and()
                    .method(HttpMethod.GET)
                    .filters { it.stripPrefix(1) }
                    .uri("lb://emails")
            }
        return router.build()
    }
}
