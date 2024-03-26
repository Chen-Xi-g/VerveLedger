import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * 功能模块约定插件
 *
 * @author 高国峰
 * @date 2023/9/17-13:39
 */
class FeatureConventionPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        with(target) {
            // 获取libs版本管理
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            pluginManager.apply {
                apply("griffin.library")
                apply("griffin.hilt")
            }
            dependencies {
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-appcompat").get())
                add("implementation", libs.findLibrary("material").get())
                add("implementation", libs.findLibrary("androidx-activity").get())
                add("implementation", libs.findLibrary("androidx-fragment").get())
                add("implementation", libs.findLibrary("androidx-constraintlayout").get())
                add("implementation", libs.findLibrary("kotlin-coroutines").get())
                add("implementation", libs.findLibrary("lifecycle-viewmodel").get())
                add("implementation", project(":core:base"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:dialog"))
                add("implementation", project(":core:domain"))
                add("implementation", project(":core:network"))
                add("implementation", project(":core:router"))
                add("implementation", project(":core:rv"))
                add("implementation", project(":core:utils"))
            }
        }
    }
}