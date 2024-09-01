package com.template

import org.springframework.context.annotation.Configuration
import com.github.kagkarlsson.jdbc.JdbcRunner;
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class Configuration {

    @Bean
    @Primary
    fun springTransactionManager(datasource: DataSource, databaseConfig: DatabaseConfig): SpringTransactionManager {
        return SpringTransactionManager(_dataSource = datasource, databaseConfig = databaseConfig, showSql = false)
    }

    @Bean
    fun databaseConfig(): DatabaseConfig {
        return DatabaseConfig {}
    }

    @Bean
    fun createJdbcRunner(dataSource: DataSource): JdbcRunner {
        println("autoCommit: " + dataSource.connection.autoCommit)
        return JdbcRunner(dataSource, false)
    }
}