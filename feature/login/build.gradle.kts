plugins {
    id("griffin.feature")
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.griffin.feature.login"
}
dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
}
