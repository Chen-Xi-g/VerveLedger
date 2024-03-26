plugins {
    id("griffin.library")
    id("griffin.hilt")
}

android {
    namespace = "com.griffin.core.base"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(project(":core:data"))
    implementation(project(":core:utils"))
    implementation(project(":core:network"))
    implementation(project(":core:dialog"))
    implementation(libs.kotlin.coroutines)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lottie)
    implementation(libs.autoSize)
}