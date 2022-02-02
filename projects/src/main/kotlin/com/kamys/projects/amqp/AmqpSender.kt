package com.kamys.projects.amqp

import com.kamys.base.AmqpProperties
import com.kamys.base.MessageProjectEditName
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AmqpSender(
    @Autowired
    private val rabbitTemplate: RabbitTemplate,
) {

    fun sendMessage(messages: MessageProjectEditName) {
        rabbitTemplate.convertAndSend(
            AmqpProperties.ProjectEdit.exchange,
            "edit.name",
            messages
        )
    }
}