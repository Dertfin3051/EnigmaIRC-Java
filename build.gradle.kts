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
    manifest.attributes["Main-Class"] = "ru.dfhub.eirc"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}