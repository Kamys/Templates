package com.kamys.projects.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.kamys.base.AmqpProperties
import com.kamys.projects.Application
import com.kamys.projects.ProjectTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainerProvider
import org.testcontainers.containers.RabbitMQContainer
import javax.sql.DataSource


@SpringBootTest(
    classes = [BaseIntegrationTest.TestConfiguration::class, Application::class]
)
@AutoConfigureMockMvc
class BaseIntegrationTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate


    companion object {
        private val postgreSQLContainer = PostgreSQLContainerProvider().newInstance("14")
            .also {
                it.withReuse(true)
                it.start()
            }
        private val rabbitMQContainer = RabbitMQContainer("rabbitmq:3.9.13")
            .also {
                it.withReuse(true)
                it.start()
            }
    }

    fun Any.asJson(): String = objectMapper.writeValueAsString(this)

    @BeforeEach
    fun createAllTableInDataBase() {
        transaction {
            // TODO: replace on migration
            SchemaUtils.create(ProjectTable)
        }
    }

    @BeforeEach
    fun deleteAllEntitiesInDataBase() {
        transaction {
            ProjectTable.deleteAll()
        }
    }

    @BeforeEach
    fun deleteAllMessagesInAMQP() {
        // TODO: How get all queue names?
        rabbitTemplate.execute { it.queuePurge(AmqpProperties.ProjectEdit.queue) }
    }

    @Configuration
    class TestConfiguration {
        @Bean
        fun connectionFactory(): ConnectionFactory {
            val cachingConnectionFactory = CachingConnectionFactory(rabbitMQContainer.host, rabbitMQContainer.amqpPort)
            cachingConnectionFactory.setUsername(rabbitMQContainer.adminUsername)
            cachingConnectionFactory.setPassword(rabbitMQContainer.adminPassword)
            return cachingConnectionFactory
        }

        @Bean
        fun getDataSource(): DataSource? {
            val dataSourceBuilder = DataSourceBuilder.create()
            dataSourceBuilder.driverClassName(postgreSQLContainer.getDriverClassName())
            dataSourceBuilder.url(postgreSQLContainer.getJdbcUrl())
            dataSourceBuilder.username(postgreSQLContainer.getUsername())
            dataSourceBuilder.password(postgreSQLContainer.getPassword())
            return dataSourceBuilder.build()
        }
    }
}



