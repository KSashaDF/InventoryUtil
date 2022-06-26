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
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:23.0.0")
}

sourceSets {
    create("examples") {
        compileClasspath += sourceSets.main.get().output
    }
}

val examplesCompileOnly: Configuration by configurations.getting {
    extendsFrom(configurations.compileOnly.get())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}