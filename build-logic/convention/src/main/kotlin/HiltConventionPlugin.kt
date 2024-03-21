import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Hilt 依赖注入插件
 *
 * @author 高国峰
 * @date 2023/9/17-13:34
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // 获取libs版本管理
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            // 插件管理器
            with(pluginManager){
                apply(libs.getPlugin("dagger-hilt"))
            }
            dependencies {
                "implementation"(libs.findLibrary("dagger-hilt").get())
                "ksp"(libs.findLibrary("dagger-hilt-compiler").get())
            }
        }
    }

    private fun VersionCatalog.getPlugin(name: String) = this.findPlugin(name).get().get().pluginId
}