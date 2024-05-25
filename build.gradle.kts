plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
    withJavadocJar()
    withSourcesJar()
}

group = "com.joaonmatos"
version = "0.1.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenCentral") {
            artifactId = "path-parse"
            from(components["java"])
            pom {
                name = "path-parse"
                description = "A tiny, zero-dependency URI path parser for Java"
                url = "https://github.com/joaonmatos/path-parse"

                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "joaonmatos"
                        name = "João Nuno Matos"
                        email = "me@joaonmatos.com"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/joaonmatos/path-parse.git"
                    developerConnection = "scm:git:ssh://github.com:joaonmatos/path-parse.git"
                    url = "https://github.com/joaonmatos/path-parse/tree/main"
                }
            }


        }
    }
}

signing {
    sign(publishing.publications["mavenCentral"])
}
