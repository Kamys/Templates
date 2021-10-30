package com.template

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Application

fun main() {
    SpringApplication.run(Application::class.java)
}

@RestController
class Controller {

    @GetMapping()
    fun get(): String {
        return "Hello spring MVC!"
    }
}