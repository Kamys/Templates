package com.kamys.emails

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration
import java.util.concurrent.TimeoutException

class BusinessException: Exception() {} // For example

@Configuration
class Resilience4JConfig {
    @Bean
    fun circuitBreakerRegistry(): CircuitBreakerRegistry {
        val circuitBreakerConfig = CircuitBreakerConfig.custom()
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(3) // Количество вызовов которое учитываем
            .minimumNumberOfCalls(2)
            .recordExceptions(Exception::class.java, TimeoutException::class.java)
            .ignoreExceptions(BusinessException::class.java)
            .waitDurationInOpenState(Duration.ofSeconds(3))
            .build()

        return CircuitBreakerRegistry.of(circuitBreakerConfig)
    }
}