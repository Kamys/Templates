dependencies {
    implementation(project(":base"))

    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:${Versions.EXPOSED}")
    implementation("org.jetbrains.exposed:exposed-dao:${Versions.EXPOSED}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.postgresql:postgresql:42.3.1")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:${Versions.EUREKA_CLIENT}")
    implementation("org.springframework.amqp:spring-rabbit:${Versions.SPRING_RABBIT}")
    implementation("org.springframework.cloud:spring-cloud-starter-config:3.1.1")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test:${Versions.SPRING}")
    testImplementation("org.testcontainers:postgresql:${Versions.TEST_CONTAINERS}")
    testImplementation("org.testcontainers:rabbitmq:${Versions.TEST_CONTAINERS}")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.1.0")
}