package com.template

import com.kamys.projects.Project
import com.kamys.projects.ProjectTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException

@RestController
class Controller {

    @GetMapping()
    fun get(): String {
        return "Main page project server"
    }

    @GetMapping("/projects")
    fun getProjects(): List<String> {
        val names = transaction {
            ProjectTable.selectAll().map { it[ProjectTable.name] }
        }
        return names
    }

    @PutMapping("/projects/{id}")
    fun editProject(@PathVariable id: Int, @RequestBody request: RequestProjectEdit) {
        transaction {
            val project = Project.find { ProjectTable.id.eq(id) }
                .firstOrNull() ?: throw Exception("Not found")

            project.name = request.name
        }
    }
}

class RequestProjectEdit(
    val name: String,
)