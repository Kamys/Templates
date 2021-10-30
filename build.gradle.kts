import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation("org.springframework.boot:spring-boot-starter:2.5.6")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.6")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.6")
    testImplementation(kotlin("test"))
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