package com.kamys.emails


import com.kamys.base.AmqpProperties
import com.kamys.base.MessageProjectEditName
import com.kamys.emails.utils.BaseIntegrationTest
import com.kamys.emails.utils.getRandomEmail
import io.kotest.inspectors.shouldForOne
import io.kotest.matchers.shouldBe
import org.awaitility.Awaitility.await
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test


class MailTests: BaseIntegrationTest() {
    @Test
    fun `should send email when will receive message about project name edit`() {
        val message = MessageProjectEditName(
            email = getRandomEmail(),
            newName = "New project name",
            oldName = "Old project name"
        )

        rabbitTemplate.convertAndSend(
            AmqpProperties.ProjectEdit.exchange,
            "edit.name",
            message
        )

        await().until {
            transaction {
                Mail.all().empty().not()
            }
        }

        transaction {
            val textEmail = "Project «${message.oldName}» rename to «${message.newName}»"
            Mail.all().toList().shouldForOne {
                it.to.shouldBe(message.email)
                it.text.shouldBe(textEmail)
            }
        }
    }
}