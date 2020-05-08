package com.cuupa.dms.configuration

import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
open class CamundaConfiguration {

    @Value("\${application.camunda.mariadb_host}")
    private lateinit var mariadbHost: String

    @Value("\${application.camunda.mariadb_port}")
    private lateinit var mariadbPort: String

    @Value("\${application.camunda.databasename}")
    private lateinit var databasename: String

    @Value("\${application.camunda.camunda_user}")
    private lateinit var camundaUser: String

    @Value("\${application.camunda.camunda_password}")
    private lateinit var camundaPassword: String

    //@Bean
    open fun datasource(): DataSource {
        val datasourceBuilder = DataSourceBuilder.create()
        datasourceBuilder.driverClassName("org.mariadb.jdbc.Driver")
        datasourceBuilder.url("jdbc:mariadb://$mariadbHost:$mariadbPort/$databasename")
        datasourceBuilder.username(camundaUser)
        datasourceBuilder.password(camundaPassword)
        return datasourceBuilder.build()
    }

    @Bean
    open fun transactionManager(): PlatformTransactionManager {
        return DataSourceTransactionManager(datasource())
    }


    @Bean
    open fun processEngineConfiguration(): SpringProcessEngineConfiguration? {
        val config = SpringProcessEngineConfiguration()
        config.processEngineName = "dms"
        config.dataSource = datasource()
        config.transactionManager = transactionManager()
        config.databaseSchemaUpdate = "true"
        config.history = "full"
        config.isJobExecutorActivate = true
        return config
    }

    @Bean
    open fun processEngine(): ProcessEngineFactoryBean? {
        val factoryBean = ProcessEngineFactoryBean()
        factoryBean.processEngineConfiguration = processEngineConfiguration()
        return factoryBean
    }

}