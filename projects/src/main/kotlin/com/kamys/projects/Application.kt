package com.template

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@SpringBootApplication
@EnableDiscoveryClient
class Application

object ProjectTable : Table() {
    val id = text("id")
    val name = text("name")
    val email = text("email")
}

fun main() {
    SpringApplication.run(Application::class.java)

    println("Client run!")

    transaction {
        SchemaUtils.create(ProjectTable)
        if (ProjectTable.selectAll().count().toInt() == 0) {
            repeat(3) { index ->
                ProjectTable.insert {
                    it[ProjectTable.id] = Random.nextInt().toString()
                    it[ProjectTable.name] = "Project $index"
                    it[ProjectTable.email] = "test$index@gmail.com"
                }
            }
        }
    }
}

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
}
