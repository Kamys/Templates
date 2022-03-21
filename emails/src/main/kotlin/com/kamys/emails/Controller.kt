package com.kamys.emails

import com.kamys.base.ProjectView
import feign.RequestLine
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
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
    @GetMapping("/notification-all-user")
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

    @GetMapping("/notification-favorite")
    @CircuitBreaker(name = "ApiGatewayClient", fallbackMethod = "notificationFavoriteFallback")
    fun notificationFavorite(): List<String> {
        val projects = apiGatewayClient.getFavouritesProject()
        transaction {
            projects.forEach {
                Mail.new {
                    this.to = it.email
                    this.text = "Message for favourites project"
                }
            }
        }

        return projects.map { it.name }
    }

    fun notificationFavoriteFallback(throwable: Throwable): List<String> {
        return listOf("Fallback data")
    }

    @GetMapping("/notification-favorite-2")
    fun notificationFavoriteWithoutCircuitBreaker(): List<String> {
        val projects = apiGatewayClient.getFavouritesProject()
        transaction {
            projects.forEach {
                Mail.new {
                    this.to = it.email
                    this.text = "Message for favourites project"
                }
            }
        }

        return projects.map { it.name }
    }
}

@Component
@FeignClient(name = "apiGateway")
interface ApiGatewayClient {
    @RequestLine("GET /projects/")
    @RequestMapping(method = [RequestMethod.GET], value = ["/projects/"], consumes = ["application/json"])
    fun getAllProject(): List<ProjectView>
    @RequestLine("GET /projects/favourites")
    @RequestMapping(method = [RequestMethod.GET], value = ["/projects/favourites"], consumes = ["application/json"])
    fun getFavouritesProject(): List<ProjectView>
}