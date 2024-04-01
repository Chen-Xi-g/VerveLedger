plugins {
    id("griffin.feature")
}

android {
    namespace = "com.griffin.feature.map"
}

dependencies {
    implementation(libs.map)
    implementation(libs.map.search)
}
