package com.kamys.projects

import org.springframework.amqp.rabbit.test.context.SpringRabbitTest
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(
    classes = [MockAMQPConfiguration::class, Application::class]
)
@SpringRabbitTest
class BaseIntegrationTest

