package com.kamys.projects.amqp

import com.kamys.base.AmqpProperties
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean


@SpringBootConfiguration
class AmqpConfiguration {

    @Bean
    fun amqpAdmin(
        @Autowired connectionFactory: ConnectionFactory,
    ): AmqpAdmin {
        val amqpAdmin = RabbitAdmin(connectionFactory)
        val queue = Queue(AmqpProperties.ProjectEdit.queue)
        val exchange = TopicExchange(AmqpProperties.ProjectEdit.exchange)
        amqpAdmin.declareQueue(queue)
        amqpAdmin.declareExchange(exchange)
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("edit.#"))
        return amqpAdmin
    }


}