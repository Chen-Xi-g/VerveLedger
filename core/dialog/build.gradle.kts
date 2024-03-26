plugins {
    id("griffin.library")
    id("griffin.hilt")
}

android {
    namespace = "com.griffin.core.dialog"
}

dependencies {
    implementation(libs.androidx.constraintlayout)
    implementation(libs.lottie)
    implementation(project(":core:utils"))
}