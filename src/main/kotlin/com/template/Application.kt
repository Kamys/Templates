package com.template

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableEurekaServer
class Application

object UserTable : Table() {
    val id = varchar("id", 10)
    val name = varchar("name", length = 50)
}

fun main() {
    SpringApplication.run(Application::class.java)

    transaction {
        UserTable.selectAll().forEach {
            println(it[UserTable.name])
        }
    }
}

@RestController
class Controller {
    @GetMapping()
    fun get(): String {
        return "Hello spring MVC!"
    }
}