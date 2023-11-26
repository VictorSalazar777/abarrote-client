plugins {
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks
    .withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>()
    .configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

dependencies {
    implementation(project(path = ":domain-model"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}