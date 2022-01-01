package com.kamys.projects

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object MailTable: IntIdTable() {
    val to = text("to")
    val text = text("text")
}

class Mail(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Mail>(MailTable)

    var to by MailTable.to
    var text by MailTable.text
}