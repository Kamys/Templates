package com.kamys.emails

import com.kamys.base.Projects
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.commons.util.InetUtils
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean
import org.springframework.context.annotation.Bean

@ConfigurationPropertiesScan("com.kamys")
@SpringBootApplication
@EnableDiscoveryClient
class Application {
    @Bean
    fun eurekaInstanceConfig(inetUtils: InetUtils?): EurekaInstanceConfigBean? {
        val bean = EurekaInstanceConfigBean(inetUtils)
        bean.appname = Projects.EMAIL_SERVICE
        return bean
    }
}

fun main() {
    SpringApplication.run(Application::class.java)

    println("Client run!")

    transaction {
        SchemaUtils.create(MailTable)
    }
}

