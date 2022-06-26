plugins {
    id("java")
    id("maven-publish")
}

group = "me.sashak"
version = "1.0-SNAPSHOT"

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

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            setUrl("https://maven.pkg.github.com/KSashaDF/InventoryUtil")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}