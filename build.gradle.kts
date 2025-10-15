plugins {
    id("java")
}

group = "kz.education"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
    implementation("com.google.code.gson:gson:${property("gsonVersion")}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junitVersion")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junitVersion")}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${property("junitPlatformVersion")}")
    testCompileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
    testAnnotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")

    testImplementation("org.mockito:mockito-core:${property("mockitoVersion")}")
}

tasks.test {
    useJUnitPlatform()
}