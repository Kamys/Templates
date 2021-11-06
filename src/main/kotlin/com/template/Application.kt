package com.template

import org.jetbrains.exposed.sql.Table
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableDiscoveryClient
class Application

object UserTable : Table() {
    val id = varchar("id", 10)
    val name = varchar("name", length = 50)
}

fun main() {
    SpringApplication.run(Application::class.java)

    println("Client run!")
}

@RestController
class Controller {
    @GetMapping()
    fun get(): String {
        return "Discovery client!"
    }
}