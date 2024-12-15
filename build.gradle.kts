import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.gradleup.shadow") version "8.3.3" apply false
}

group = "ru.dfhub.eirc"
version = "2.1.1"

allprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    apply(plugin = "java")
    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

subprojects {
    version = rootProject.version
    group = rootProject.group

    apply(plugin = "com.gradleup.shadow")

    dependencies {
        implementation("org.json:json:20240303")
        if (project.name !== "common") implementation(project(":common"))
    }

    tasks.build {
        dependsOn(tasks.withType<ShadowJar>())
    }
}

tasks.jar { isEnabled = false }
