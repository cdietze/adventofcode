import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
    application
}

repositories {
//    mavenLocal()
    mavenCentral()
    maven(url = "https://jitpack.io") {
        // metadataSources { artifact() } // Needed to use un-tagged builds
    }
}

dependencies {
//    implementation("com.github.cdietze:parsek-jvm:unspecified")
    implementation("com.github.cdietze.parsek:parsek-jvm:ff789e40b4")
    testImplementation(kotlin("test-junit"))
}

sourceSets {
    main {
        resources {
            srcDir("src/main/kotlin")
        }
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

application {
    val main = project.findProperty("day")?.let { "advent2020.day${it}.Main" } ?: "advent2020.MainKt"
    println("Setting main class to $main")
    mainClass.set(main)
}
