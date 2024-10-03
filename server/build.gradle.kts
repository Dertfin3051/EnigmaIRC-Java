plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "ru.dfhub.eirc"
version = "1.0-SNAPSHOT"

tasks.jar {
    manifest.attributes["Main-Class"] = "ru.dfhub.eirc.Main"

    dependsOn(tasks.shadowJar)
}

repositories {
    mavenCentral()
    repositories {
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

dependencies {
    implementation("org.json:json:20240303")
    implementation("com.github.Dertfin3051:Colored:1.3")
}