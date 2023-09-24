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

application {
    mainClass.set("pan.artem.tinkoff.App")
}
