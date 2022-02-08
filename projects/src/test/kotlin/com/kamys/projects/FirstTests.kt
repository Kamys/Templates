package com.kamys.projects

import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test


class FirstTests: BaseIntegrationTest() {

    @Test
    fun `first test`() {
        (1 + 1).shouldBe(2)
        transaction {
            SchemaUtils.create(ProjectTable)
            Project.new {
                this.email = "Email"
                this.name = "Project name"
            }
            ProjectTable.selectAll().count().shouldBe(1)
        }
    }
}