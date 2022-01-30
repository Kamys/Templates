package com.kamys.projects.amqp

import com.kamys.base.AmqpProperties
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean


@SpringBootConfiguration
class AmqpConfiguration(
    @Autowired
    private val amqpProperties: AmqpProperties
) {

    @Bean
    fun queue(): Queue {
        return Queue(amqpProperties.queueName, false)
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange(amqpProperties.topicExchangeName)
    }

    @Bean
    fun binding(queue: Queue?, exchange: TopicExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with("edit.#")
    }

    @Bean
    fun container(
        connectionFactory: ConnectionFactory,
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(amqpProperties.queueName)
        return container
    }
}