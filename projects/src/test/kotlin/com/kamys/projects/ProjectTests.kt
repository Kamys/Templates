package com.kamys.projects


import com.kamys.base.AmqpProperties
import com.kamys.base.MessageProjectEditName
import com.kamys.projects.utils.BaseIntegrationTest
import com.kamys.projects.utils.EntityCreator
import com.kamys.projects.utils.getRandomString
import com.kamys.projects.utils.shouldHaveMessage
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.put

class ProjectTests: BaseIntegrationTest() {

    @Test
    fun `should send message project name edit`() {
        val project = EntityCreator.createProject(name = getRandomString(), email = getRandomString())

        val oldName = project.name
        val request = ProjectEditRequest(name = getRandomString())

        mockMvc.put("/projects/${project.id}") {
            content = request.asJson()
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent() }
        }
        transaction {
            project.refresh()
        }

        rabbitTemplate.shouldHaveMessage<MessageProjectEditName>(
            queueName = AmqpProperties.ProjectEdit.queue,
            checkMessage = {
                newName.shouldBe(request.name)
                oldName.shouldBe(oldName)
                email.shouldBe(project.email)
            }
        )
    }
}