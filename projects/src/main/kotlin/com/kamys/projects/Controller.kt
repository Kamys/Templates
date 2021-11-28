package com.template

import com.kamys.base.Projects
import com.kamys.projects.Project
import com.kamys.projects.ProjectTable
import com.netflix.discovery.EurekaClient
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.java.*
import kotlinx.coroutines.runBlocking

@RestController
class Controller {

    @GetMapping("/projects")
    fun getProjects(): List<String> {
        val names = transaction {
            ProjectTable.selectAll().map { it[ProjectTable.name] }
        }
        return names
    }

    @Autowired
    private val discoveryClient: EurekaClient? = null

    @PutMapping("/projects/{id}")
    fun editProject(@PathVariable id: Int, @RequestBody request: RequestProjectEdit) {
        val (project, oldName) = transaction {
            val project = Project.find { ProjectTable.id.eq(id) }
                .firstOrNull() ?: throw Exception("Not found")
            val oldName = project.name
            project.name = request.name
            Pair(project, oldName)
        }

        val application = discoveryClient!!.getApplication(Projects.EMAIL_SERVES)
        if (application == null) {
            throw Error("App ${Projects.EMAIL_SERVES} not found")
        }
        val instanceInfo = application.instances[0]
        val url = instanceInfo.homePageUrl

        println("Make post")
        runBlocking {
            val response = HttpClient(Java)
                .post<HttpResponse>("${url}email") {
                    headers {
                        append("Content-Type", "application/json")
                    }
                    body = """{ "to": "${project.email}", "text": "Project $oldName change name to ${project.name}" }"""
                }

            if (response.status.value != 200) {
                throw Error("Mail request failed ${response.readText()}")
            }
        }
    }
}

class RequestProjectEdit(
    val name: String,
)