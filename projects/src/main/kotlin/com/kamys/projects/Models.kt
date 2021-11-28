package com.kamys.projects

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ProjectTable: IntIdTable() {
    val name = text("name")
    val email = text("email")
}

class Project(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Project>(ProjectTable)

    var name by ProjectTable.name
    var email by ProjectTable.email
}