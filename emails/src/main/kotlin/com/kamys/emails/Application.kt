package com.kamys.emails

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@ConfigurationPropertiesScan("com.kamys")
@SpringBootApplication
@EnableDiscoveryClient
class Application

fun main() {
    SpringApplication.run(Application::class.java)

    println("Client run!")

    transaction {
        SchemaUtils.create(MailTable)
    }
}

