package com.template

import com.kamys.projects.MailTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class EmailServesApplication {

}

fun main() {
    SpringApplication.run(EmailServesApplication::class.java)

    println("Client run!")

    transaction {
        SchemaUtils.create(MailTable)
    }
}

