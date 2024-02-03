plugins {
    kotlin("jvm") version "1.9.21"
    id("org.jetbrains.kotlinx.kover") version "0.7.5"
    kotlin("plugin.serialization") version "1.9.22"
}

group = "edu.davis.cs.ecs036c"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}

// Target jvm 17 for ease of autograding
kotlin {
    jvmToolchain(17)
}