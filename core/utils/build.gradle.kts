plugins {
    id("griffin.library")
}

android {
    namespace = "com.griffin.core.utils"
}

dependencies {
    implementation(libs.kotlin.serialization)
    implementation(libs.mmkv)
}