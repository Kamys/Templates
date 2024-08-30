package com.template

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Application

fun main() {
    SpringApplication.run(Application::class.java)

    transaction {
        SchemaUtils.create (UserTable)

        UserTable.insert {
            it[UserTable.id] = "1"
            it[UserTable.name] = "Ron"
        }
    }
}

object UserTable : Table() {
    val id = varchar("id", 10)
    val name = varchar("name", length = 50)
}

@RestController
class Controller {
    @GetMapping()
    fun get(): String {
        return "Hello spring MVC!"
    }
}