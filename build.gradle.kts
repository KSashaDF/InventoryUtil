plugins {
    id("java")
    id("maven-publish")
}

group = "me.sashak"
version = "1.0.0"

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

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allJava)
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/KSashaDF/InventoryUtil")

            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("github-packages") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("InventoryUtil")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/KSashaDF/InventoryUtil.git")
                    developerConnection.set("scm:git:ssh://github.com:KSashaDF/InventoryUtil.git")
                    url.set("https://github.com/KSashaDF/InventoryUtil")
                }
            }
        }
    }
}