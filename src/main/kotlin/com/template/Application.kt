package com.template

import com.netflix.discovery.EurekaClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.commons.util.InetUtils
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

val APP_NAME = "App B"
val TARGET_APP = "App A"

@SpringBootApplication
@EnableDiscoveryClient
class Application {
    @Bean
    fun eurekaInstanceConfig(inetUtils: InetUtils?): EurekaInstanceConfigBean? {
        val bean = EurekaInstanceConfigBean(inetUtils)
        bean.appname = APP_NAME
        return bean
    }
}

fun main() {
    SpringApplication.run(Application::class.java)

    println("Client run!")
}

@RestController
class Controller {
    @Autowired
    private val discoveryClient: EurekaClient? = null

    @GetMapping("/")
    fun get(): String {
        val application = discoveryClient!!.getApplication(TARGET_APP)
        if (application == null) {
            return "App ${TARGET_APP} not found"
        }
        val instanceInfo = application.instances[0]
        val url = instanceInfo.homePageUrl
        return "Hello! This is ${APP_NAME}. You can go: <a href=\"${url}\">${TARGET_APP}</a>"
    }
}