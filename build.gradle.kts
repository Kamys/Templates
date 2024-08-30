import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val springVersion = "3.3.3"
val exposedVersion = "0.53.0"

plugins {
    id("org.springframework.boot") version "3.3.3"
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
}

group = "com.template"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.postgresql:postgresql:42.7.4")
}

tasks.test {
    useJUnit()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}