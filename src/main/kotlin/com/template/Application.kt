package com.template

import com.github.kagkarlsson.jdbc.JdbcRunner
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import com.github.kagkarlsson.jdbc.PreparedStatementSetter
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.time.LocalDateTime

@SpringBootApplication
class Application

fun main() {
    val context = SpringApplication.run(Application::class.java)
    transaction {
        SchemaUtils.create(UserTable)
        UserTable.deleteAll()
    }

    val service = context.getBean(MyService::class.java)
    service.createUser()

    val count = transaction { UserTable.selectAll().count() }
    if (count == 0L) {
        println("Users count: $count")
    } else {
        System.err.println("Users count: $count")
    }
    context.close()
    System.exit(0)
}

@Service
class MyService(
    private val jdbcRunner: JdbcRunner,
) {
    fun createUser() {
        runCatching {
            transaction {
                println("isActualTransactionActive: " + TransactionSynchronizationManager.isActualTransactionActive())
                UserTable.insert {
                    it[UserTable.id] = "1"
                    it[UserTable.name] = "Ron " + LocalDateTime.now()
                }
                jdbcRunner.execute(
                    "insert into test.user (id, name) values ('2', 'jdbcRunner')",
                    PreparedStatementSetter.NOOP
                )
                throw Exception("My exception for test")
            }
        }
    }
}

