package com.kamys.projects

import com.kamys.base.MessageProjectEditName
import com.kamys.base.ProjectView
import com.kamys.projects.amqp.AmqpSender
import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.concurrent.schedule


@RestController
class Controller(
    @Autowired
    val amqpSender: AmqpSender
) {

    @Value("\${greeting}")
    var greeting: String? = null

    @Value("\${eureka.instance.instance-id}")
    var instanceId: String? = null

    @GetMapping
    fun greeting(): String? {
        return "Spring env property 'greeting': $greeting"
    }

    @GetMapping("/")
    fun getProjects(): List<ProjectView> {
        return transaction {
            Project.all().map {
                ProjectView(
                    name = it.name,
                    email = it.email
                )
            }
        }
    }

    var callCount = 0
    var timerTask: TimerTask? = null

    @GetMapping("/favourites")
    fun getFavoriteProjects(): List<ProjectView> {

        if (timerTask != null) {
            throw Exception("Failed favorite projects")
        }

        if (callCount > 2) {
            timerTask = Timer("SettingUp", false).schedule(3 * 1000) {
                callCount = 0
                timerTask = null
            }
            throw Exception("Failed favorite projects")
        }

        callCount++

        return listOf(
            ProjectView(
                name = "Favorite project from server 1",
                email = "project@mail.com"
            ),
            ProjectView(
                name = "Favorite project from server 2",
                email = "project@mail.com"
            )
        )
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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

    @GetMapping("/instance-name")
    fun getInstanceName(): String? {
        return instanceId
    }
}

class ProjectEditRequest(
    val name: String,
)