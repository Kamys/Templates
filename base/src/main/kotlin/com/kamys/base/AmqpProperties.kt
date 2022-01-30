package com.kamys.base;

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "amqp")
class AmqpProperties(
    val topicExchangeName: String,
    val queueName: String,
)
