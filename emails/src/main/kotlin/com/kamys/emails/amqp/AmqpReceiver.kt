package com.kamys.emails.amqp

import com.kamys.base.MessageProjectEditName
import com.kamys.emails.Mail
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component


@Component
class AmqpReceiver {
    fun receiveMessage(message: MessageProjectEditName) {
        println("Rename project ${message.oldName}")
        transaction {
            Mail.new {
                this.to = message.email
                this.text = "Project «${message.oldName}» rename to «${message.newName}»"
            }
        }
    }
}