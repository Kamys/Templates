package com.template

import org.jetbrains.exposed.sql.Table

object UserTable : Table("user") {
    val id = varchar("id", 10)
    val name = varchar("name", length = 50)
}