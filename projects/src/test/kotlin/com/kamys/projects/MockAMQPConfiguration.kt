package com.kamys.projects

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import org.mockito.ArgumentMatchers.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.springframework.amqp.rabbit.connection.Connection
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean

@SpringBootConfiguration
class MockAMQPConfiguration {
    @Bean
    fun connectionFactory(): ConnectionFactory {
        val declareOk = mock<AMQP.Queue.DeclareOk>()

        val channel = mock<Channel> {
            on { queueDeclare() } doReturn declareOk
            on { queueDeclare(anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyMap()) } doReturn declareOk
            on { isOpen } doReturn true
        }

        val connection = mock<Connection> {
            on { createChannel(anyBoolean()) } doReturn channel
        }

        val connectionFactory = mock<ConnectionFactory> {
            on { createConnection() } doReturn connection
        }

        return connectionFactory
    }
}