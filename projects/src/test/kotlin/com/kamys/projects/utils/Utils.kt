package com.kamys.projects.utils

import io.kotest.assertions.fail
import org.springframework.amqp.rabbit.core.RabbitTemplate
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

fun getRandomString(length: Int = 50) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun getRandomEmail(length: Int = 20) : String {
    return getRandomString(length) + "@mail.com"
}

fun <T> RabbitTemplate.getMessage(queueName: String): T? {
    return this.execute {
        val response = it.basicGet(queueName, true)
        if (response != null) {
            return@execute deserialize<T>(response.body)
        } else {
            return@execute null
        }
    }
}

inline fun <reified T> RabbitTemplate.shouldHaveMessage(
    queueName: String,
    checkMessage: T.() -> Unit = {}
): T {
    val message = this.getMessage<T>(queueName)

    if (message == null) {
        fail("Queue '${queueName}' don't have message")
    }

    message.checkMessage()

    return message
}

@Suppress("UNCHECKED_CAST")
private fun <T>deserialize(bytes: ByteArray): T {
    ByteArrayInputStream(bytes).use { b ->
        ObjectInputStream(b).use { o ->
            return o.readObject() as T
        }
    }
}