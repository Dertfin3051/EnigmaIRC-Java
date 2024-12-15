import com.google.cloud.tools.jib.api.buildplan.ImageFormat
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

plugins {
    application
    id("com.google.cloud.tools.jib") version "3.4.4"
}

application {
    mainClass = "ru.dfhub.eirc.Main"
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-core:2.24.1")
    implementation("org.apache.logging.log4j:log4j-api:2.24.1")
}

jib {
    containerizingMode = "packaged"
    to.image = "dertfin1/enigmairc-server:${version}"
    from.image = "eclipse-temurin:${java.targetCompatibility.majorVersion}-jre-alpine"
    container.ports = listOf("6667/tcp")
    container.format = ImageFormat.Docker

    // what da hail is dat
    ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().let {
        container.creationTime = it
        container.filesModificationTime = it
    }

    outputPaths {
        // gradle layout apu is kinda bad, u know???????
        project.layout.buildDirectory.asFile.get().resolve("jib").let {
            tar = it.resolve("jib-image.tar").absolutePath
            digest = it.resolve("jib-image.digest").absolutePath
            imageId = it.resolve("jib-image.id").absolutePath
            imageJson = it.resolve("jib-image.json").absolutePath
        }
    }
}

tasks.build {
    dependsOn(tasks.jibBuildTar)
}
