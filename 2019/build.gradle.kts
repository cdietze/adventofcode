plugins {
    kotlin("jvm") version "1.3.60"

    // Apply the application to add support for building a CLI application
    application
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.cdietze:parsek:f61445f")
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
