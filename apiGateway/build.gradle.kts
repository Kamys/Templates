dependencies {
    implementation(project(":base"))

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:${Versions.EUREKA_CLIENT}")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:3.1.1")
}
