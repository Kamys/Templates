package com.kamys.projects.utils

import com.kamys.projects.Project
import org.jetbrains.exposed.sql.transactions.transaction

object EntityCreator {
    fun createProject(
        name: String = getRandomString(),
        email: String = getRandomEmail()
    ): Project {
        return transaction {
            Project.new {
                this.email = email
                this.name = name
            }
        }
    }
}