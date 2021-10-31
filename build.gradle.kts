import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val exposedVersion = "0.34.1"
val springVersion = "2.5.6"

plugins {
    kotlin("jvm") version "1.5.30"
    application

    id("org.jetbrains.kotlin.plugin.spring") version "1.5.31"
}

group = "com.template"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.postgresql:postgresql:42.3.1")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("Application.kt")
}