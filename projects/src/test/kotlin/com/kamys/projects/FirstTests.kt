package com.kamys.projects

§
import com.kamys.projects.utils.BaseIntegrationTest
import com.kamys.projects.utils.EntityCreator
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.put


class FirstTests(
    @Autowired
    private val rabbitTemplate: RabbitTemplate,
): BaseIntegrationTest() {

    @Test
    fun `first test`() {

        transaction {
            SchemaUtils.create(ProjectTable)
        }
        val project = EntityCreator.createProject()
        val request = ProjectEditRequest(name = "New name for project")

        mockMvc.put("/projects/${project.id}") {
            content = request.asJson()
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent() }
        }

        println("== expectedQueueNames ==")
        /*rabbitTemplate.expectedQueueNames().forEach {
            print(it)
        }*/

        1.shouldBe(1)
        println("project.name: ${project.name}")
    }
}