import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"

    // -----------------------------------------------------------------------------
    // 参考：https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint
    // $ gradle ktlintFormat
    // -----------------------------------------------------------------------------
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.hsqldb:hsqldb")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// ##############################################################################
// 手書き
// ##############################################################################
tasks.register("downloadDependencies") {
    doLast {
        val allDeps = configurations.names
            .map { configurations[it] }
            .filter { it.isCanBeResolved }
            .map { it.resolve().size }
            .sum()
        println("Downloaded all dependencies: $allDeps")
    }
}
