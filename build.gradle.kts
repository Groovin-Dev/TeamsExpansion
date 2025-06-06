import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

var group = property("group")!!
var version = property("version")!!

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_21.toString()
        kotlinOptions.javaParameters = true
    }

    withType<JavaCompile>() {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    withType<Javadoc>() {
        options.encoding = "UTF-8"
    }

    register("createVersionFile") {
        val versionFile = file("${layout.buildDirectory.get()}/resources/main/version.txt")
        outputs.file(versionFile)

        doLast {
            versionFile.parentFile.mkdirs()
            versionFile.writeText(project.version.toString())
        }
    }

    register("printVersion") {
        doLast {
            println(project.version)
        }
    }

    processResources {
        dependsOn("createVersionFile")

        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveBaseName.set("${project.name}-${project.version}")
        archiveClassifier.set("")
        archiveVersion.set("")

        from(sourceSets["main"].output)
    }
}
