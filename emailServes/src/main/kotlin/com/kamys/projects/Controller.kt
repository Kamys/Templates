package com.template

import com.kamys.projects.Mail
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.web.bind.annotation.*

@RestController
class Controller {

    @PostMapping("/email")
    fun sendMail(@RequestBody request: RequestMailCreate): String {
        println("sendMail(${request.text})")
        transaction {
            Mail.new {
                this.to = request.to
                this.text = request.text
            }
        }
        return "New email created"
    }
}

class RequestMailCreate(
    val to: String,
    val text: String,
)