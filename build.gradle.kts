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
}

subprojects {
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "application")

    tasks.build {
        dependsOn(tasks.withType<ShadowJar>())
    }

    dependencies {
        implementation("org.json:json:20240303")
    }
}

tasks.jar { isEnabled = false }
