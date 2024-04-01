plugins {
    id("griffin.library")
    id("griffin.hilt")
}

android {
    namespace = "com.griffin.core.domain"
}

dependencies {
    implementation(project(":core:data"))
}