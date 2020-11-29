import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61"

    // Apply the application to add support for building a CLI application
    application
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io") {
        // metadataSources { artifact() } // Needed to use un-tagged builds
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.cdietze.parsek:parsek-jvm:v0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
}

sourceSets {
    main {
        resources {
            srcDir("src/main/kotlin")
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

application {
    val day = project.findProperty("day") ?: "01"
    println("Running day $day")
    mainClassName = "advent2019.day$day.MainKt"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}
