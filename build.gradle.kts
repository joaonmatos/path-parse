plugins {
    // Apply the java-library plugin for API and implementation separation.
    id("java-library")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

group = "com.joaonmatos.utils"
version = "1.0-SNAPSHOT"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

// Apply a specific Java toolchain to ease working on different environments.

tasks.test {
    useJUnitPlatform()
}
