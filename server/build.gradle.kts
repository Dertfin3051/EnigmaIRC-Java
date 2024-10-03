plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "ru.dfhub.eirc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    repositories {
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

dependencies {
    implementation("com.github.Dertfin3051:Colored:1.3")
}