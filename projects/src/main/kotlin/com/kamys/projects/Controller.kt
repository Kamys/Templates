package com.kamys.projects

import com.kamys.base.MessageProjectEditName
import com.kamys.projects.amqp.AmqpSender
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class Controller(
    @Autowired
    val amqpSender: AmqpSender
) {

    @GetMapping("/projects")
    fun getProjects(): List<String> {
        val names = transaction {
            ProjectTable.selectAll().map { it[ProjectTable.name] }
        }
        return names
    }

    @PutMapping("/projects/{id}")
    fun editProject(@PathVariable id: Int, @RequestBody request: ProjectEditRequest) {
        val (project, oldName) = transaction {
            val project = Project.find { ProjectTable.id.eq(id) }
                .firstOrNull() ?: throw Exception("Not found")
            val oldName = project.name
            project.name = request.name
            Pair(project, oldName)
        }

        amqpSender.sendMessage(
            MessageProjectEditName(
                email = project.email,
                oldName = oldName,
                newName = request.name
            )
        )
    }
}

class ProjectEditRequest(
    val name: String,
)