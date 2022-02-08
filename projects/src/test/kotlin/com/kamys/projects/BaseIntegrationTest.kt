package com.kamys.projects

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.PostgreSQLContainerProvider
import org.testcontainers.containers.RabbitMQContainer


@SpringBootTest(
    classes = [BaseIntegrationTest.TestAMQPConfiguration::class, Application::class]
)
class BaseIntegrationTest {
    companion object {
        private val postgreSQLContainer = PostgreSQLContainerProvider().newInstance("14")
            .also {
                it.withReuse(true)
                it.start()
            }
        private val rabbitMQContainer = RabbitMQContainer("rabbitmq:3.9.13")
            .also {
                it.withReuse(true)
                it.start()
            }
    }


    @Configuration
    class TestAMQPConfiguration {
        @Bean
        fun connectionFactory(): ConnectionFactory {
            val cachingConnectionFactory = CachingConnectionFactory(rabbitMQContainer.host, rabbitMQContainer.amqpPort)
            cachingConnectionFactory.setUsername(rabbitMQContainer.adminUsername)
            cachingConnectionFactory.setPassword(rabbitMQContainer.adminPassword)
            return cachingConnectionFactory
        }
    }
}



