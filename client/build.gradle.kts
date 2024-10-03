plugins {
    id("java")
}

group = "ru.dfhub.eirc"
version = "1.0-SNAPSHOT"

tasks.jar {
    manifest.attributes["Main-Class"] = "ru.dfhub.eirc.Main"
}

repositories {
    mavenCentral()
}

dependencies {
}