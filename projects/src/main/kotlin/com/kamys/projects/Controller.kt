package com.template

import com.kamys.projects.Project
import com.kamys.projects.ProjectTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*


@RestController
class Controller(
    @Autowired
    val emailServerClient: EmailServerClient
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

        println("Make post")
        emailServerClient.sendMail(
            NewEmailRequest(
                to = project.email,
                text = "Project $oldName change name to ${project.name}"
            )
        )
    }
}

class ProjectEditRequest(
    val name: String,
)

class NewEmailRequest(
    val to: String,
    val text: String
)

@FeignClient(name = "test", url = "http://localhost:8081")
interface EmailServerClient {
    @RequestMapping(method = [RequestMethod.POST], value = ["/email"], consumes = ["application/json"])
    fun sendMail(email: NewEmailRequest)
}