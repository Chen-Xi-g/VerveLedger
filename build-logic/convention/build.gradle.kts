plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        // Application 插件
        register("androidApplication"){
            id = "griffin.application"
            implementationClass = "ApplicationPlugin"
        }
        // Library 插件
        register("androidLibrary"){
            id = "griffin.library"
            implementationClass = "LibraryPlugin"
        }
        // Feature 插件
        register("androidFeature"){
            id = "griffin.feature"
            implementationClass = "FeatureConventionPlugin"
        }
        // Hilt 插件
        register("androidHilt"){
            id = "griffin.hilt"
            implementationClass = "HiltConventionPlugin"
        }
    }
}