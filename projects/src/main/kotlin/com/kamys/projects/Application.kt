package com.kamys.projects

import com.kamys.base.Projects
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.commons.util.InetUtils
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import kotlin.random.Random

@ConfigurationPropertiesScan("com.kamys")
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
class Application {
    @Bean
    fun eurekaInstanceConfig(inetUtils: InetUtils?): EurekaInstanceConfigBean? {
        val bean = EurekaInstanceConfigBean(inetUtils)
        bean.appname = Projects.PROJECTS
        return bean
    }
}

fun main() {
    SpringApplication.run(Application::class.java)

    println("Client run!")

    transaction {
        SchemaUtils.create(ProjectTable)
        if (ProjectTable.selectAll().count().toInt() == 0) {
            repeat(3) { index ->
                ProjectTable.insert {
                    it[ProjectTable.id] = Random.nextInt()
                    it[name] = "Project $index"
                    it[email] = "test$index@gmail.com"
                }
            }
        }
    }
}

