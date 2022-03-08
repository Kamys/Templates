package com.kamys.emails

import com.kamys.base.ProjectView
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    @Autowired
    val apiGatewayClient: ApiGatewayClient
) {
    @GetMapping("/notificationAllUser")
    fun notificationAllUser() {
        val projects = apiGatewayClient.getAllProject()
        transaction {
            projects.forEach {
                Mail.new {
                    this.to = it.email
                    this.text = "Message for all project"
                }
            }
        }
    }
}

@Component
@FeignClient(name = "apiGateway")
interface ApiGatewayClient {
    @RequestMapping(method = [RequestMethod.GET], value = ["/projects/"], consumes = ["application/json"])
    fun getAllProject(): List<ProjectView>
}