plugins {
    java
}

group = "me.sashak"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:23.0.0")
}

sourceSets {
    create("examples") {
        compileClasspath += sourceSets.main.get().output
    }
}

val examplesImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}