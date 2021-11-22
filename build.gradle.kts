import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application

    kotlin("jvm") version "1.5.30"
    kotlin("plugin.spring") version Versions.KOTLIN_VERSION
    id("org.springframework.boot") version Versions.SPRING
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

allprojects {
    group = "com.template"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("org.gradle.application")
        plugin("kotlin")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.springframework.boot")
    }

    apply(plugin = "io.spring.dependency-management")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter:${Versions.SPRING}")
        implementation("org.springframework.boot:spring-boot-starter-web:${Versions.SPRING}")
        testImplementation(kotlin("test"))
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Versions.SPRING_CLOUD}")
        }
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}