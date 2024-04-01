plugins {
    id("griffin.library")
}

android {
    namespace = "com.griffin.core.data"
}

dependencies {
    implementation(project(":core:rv"))
    implementation(libs.kotlin.serialization)
}