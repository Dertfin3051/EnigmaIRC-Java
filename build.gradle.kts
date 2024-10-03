plugins {
    id("java")
    id("application")
}

group = "ru.dfhub.eirc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.jar {
    manifest.attributes["Main-Class"] = "ru.dfhub.eirc.Main"
}

dependencies {
}