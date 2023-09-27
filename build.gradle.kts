import org.gradle.jvm.tasks.Jar

plugins {
    application
    id("io.freefair.lombok") version "8.3"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName = "${project.name}-fat"
    manifest {
        attributes["Main-Class"] = "pan.artem.tinkoff.App"
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}

application {
    mainClass.set("pan.artem.tinkoff.App")
}
