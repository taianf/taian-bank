import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    val kotlinVersion = "1.4.10"
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

group = "br.com.taian.bank"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.github.kittinunf.fuel:fuel:2.3.0")
    implementation("commons-validator:commons-validator:1.7")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2") // JVM dependency
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("org.hsqldb:hsqldb")
    testImplementation("com.ninja-squad:springmockk:1.1.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}