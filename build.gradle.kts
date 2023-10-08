import org.gradle.jvm.tasks.Jar

plugins {
    java
    id("io.freefair.lombok") version "8.3"
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "pan.artem.tinkoff"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.3")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.15")
    implementation("io.github.resilience4j:resilience4j-ratelimiter:2.1.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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

tasks.withType<Test> {
    useJUnitPlatform()
}
