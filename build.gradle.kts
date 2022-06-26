plugins {
    java
}

group = "me.sashak"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:23.0.0")
}

sourceSets {
    create("examples") {
        compileClasspath += sourceSets.main.get().output
    }
}

val examplesImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}