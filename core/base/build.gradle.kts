plugins {
    id("griffin.library")
}

android {
    namespace = "com.griffin.core.base"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(project(":core:data"))
    implementation(project(":core:utils"))
    implementation(libs.kotlin.coroutines)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lottie)
    implementation(libs.viewbinding.util)
    implementation(libs.autoSize)
}