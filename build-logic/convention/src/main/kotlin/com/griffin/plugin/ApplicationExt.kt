package com.griffin.plugin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

internal fun Project.configureApplication(
    libs: VersionCatalog,
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        // 编译版本
        compileSdk = 34

        // 默认配置
        defaultConfig {
            // 最小支持版本
            minSdk = 24

            // 字符串资源优化, 只保留英文\简体
            resourceConfigurations.addAll(listOf("en", "zh"))

            // ndk配置
            ndk {
                // 一般情况下，只需要支持armeabi-v7a和arm64-v8a
                abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
            }

            vectorDrawables {
                // 使用AndroidX的矢量图
                useSupportLibrary = true
            }
        }

        // 签名配置
        signingConfigs {
            getByName("debug") {
                // 设置根目录下mvvm.jks
                storeFile = file("../ledger.jks")
                storePassword = "123456"
                keyAlias = "ledger"
                keyPassword = "123456"
            }
            create("release") {
                // 设置根目录下mvvm.jks
                storeFile = file("../ledger.jks")
                storePassword = "123456"
                keyAlias = "ledger"
                keyPassword = "123456"
            }
        }

        // 构建类型配置
        buildTypes {
            // debug
            getByName("debug") {
                // 是否开启混淆
                isMinifyEnabled = false
            }
            // release
            getByName("release") {
                // 是否开启混淆
                isMinifyEnabled = false
            }
        }

        // 编译配置
        compileOptions {
            // 是否开启Java17支持
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        // kotlin配置
        kotlinOptions {
            // 是否开启Java17支持
            jvmTarget = "17"
        }

        buildFeatures {
            dataBinding{
                enable = true
            }
        }

        // 指定选项和规则
        packaging {
            // 排除META-INF目录下的AL2.0和LGPL2.1文件
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        dependencies {
            add("implementation", libs.findLibrary("androidx.core.ktx").get())
            add("implementation", libs.findLibrary("androidx.appcompat").get())
            add("implementation", libs.findLibrary("material").get())
            add("implementation", libs.findLibrary("androidx.activity").get())
            add("implementation", libs.findLibrary("androidx.fragment").get())
            add("implementation", libs.findLibrary("androidx.constraintlayout").get())
            add("ksp", libs.findLibrary("router.apt").get())
            add("implementation", libs.findLibrary("router.router").get())
        }
    }
}

internal fun Project.configureLibrary(
    libs: VersionCatalog,
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        // 编译版本
        compileSdk = 34

        // 默认配置
        defaultConfig {
            // 最小支持版本
            minSdk = 24

            // 字符串资源优化, 只保留英文\简体
            resourceConfigurations.addAll(listOf("en", "zh"))

            // ndk配置
            ndk {
                // 一般情况下，只需要支持armeabi-v7a和arm64-v8a
                abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
            }

            vectorDrawables {
                // 使用AndroidX的矢量图
                useSupportLibrary = true
            }
        }

        // 编译配置
        compileOptions {
            // 是否开启Java17支持
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        // kotlin配置
        kotlinOptions {
            // 是否开启Java17支持
            jvmTarget = JavaVersion.VERSION_17.toString()
        }

        // 构建类型配置
        buildTypes {
            // debug
            getByName("debug") {
                // 是否开启混淆
                isMinifyEnabled = false
            }
            // release
            getByName("release") {
                // 是否开启混淆
                isMinifyEnabled = false
            }
        }

        buildFeatures {
            dataBinding{
                enable = true
            }
        }

        // 指定选项和规则
        packaging {
            // 排除META-INF目录下的AL2.0和LGPL2.1文件
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        dependencies {
            add("ksp", libs.findLibrary("router.apt").get())
            add("implementation", libs.findLibrary("router.router").get())
        }
    }
}

internal fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}