import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val springVersion = "2.7.18"
val exposedVersion = "0.40.1"

plugins {
    id("org.springframework.boot") version "2.7.18"
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

    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.postgresql:postgresql:42.7.4")
    val pathToProject = "/Users/nikitakamyshenko/Projects/IdeaProjects/micro-jdbc"
    implementation(files("$pathToProject/target/micro-jdbc-0.7.0-SNAPSHOT.jar"))

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}