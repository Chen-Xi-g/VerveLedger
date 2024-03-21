// 表示某个功能正在孵化。这意味着该功能目前正在开发中，并且可能随时更改。它会在未来的一些 Android Studio 和插件版本中被标记为稳定。
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")