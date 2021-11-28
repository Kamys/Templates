package com.template

import com.kamys.projects.ProjectTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import kotlin.random.Random

@SpringBootApplication
@EnableDiscoveryClient
class ProjectsApplication

fun main() {
    SpringApplication.run(ProjectsApplication::class.java)

    println("Client run!")

    transaction {
        SchemaUtils.create(ProjectTable)
        if (ProjectTable.selectAll().count().toInt() == 0) {
            repeat(3) { index ->
                ProjectTable.insert {
                    it[ProjectTable.id] = Random.nextInt()
                    it[ProjectTable.name] = "Project $index"
                    it[ProjectTable.email] = "test$index@gmail.com"
                }
            }
        }
    }
}

