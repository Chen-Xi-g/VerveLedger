plugins {
    id("griffin.application")
    id("griffin.hilt")
    id("therouter")
}

android {
    namespace = "com.griffin.ledger"

    defaultConfig {
        applicationId = "com.griffin.ledger"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(project(":core:base"))
    implementation(project(":core:router"))
    implementation(project(":core:utils"))
    implementation(project(":feature:login"))
    implementation(project(":feature:home"))
    implementation(project(":feature:add"))
    implementation(project(":feature:account"))
    implementation(project(":feature:map"))
    implementation(project(":feature:type"))
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.splash.screen)
}